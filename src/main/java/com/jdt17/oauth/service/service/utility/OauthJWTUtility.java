package com.jdt17.oauth.service.service.utility;

import com.jdt17.oauth.service.components.runtime.exception.CoreThrowHandler;
import com.jdt17.oauth.service.configuration.JWTConfiguration;
import com.jdt17.oauth.service.data.module.UserDTO;
import com.jdt17.oauth.service.data.response.RestApiError;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class OauthJWTUtility {

    private final MessageSource messageSource;

    public String generateOAUTHToken(
            UserDTO userDTO,
            int TTL,
            String remoteIP
    )  {
        try {
            // 1. Define token issuance and expiration times
            Instant ISSUED_AT = Instant.now();
            Instant EXPIRE_AT = ISSUED_AT.plusSeconds(TTL);

            // 2. Load RSA public key for encryption
            RSAPublicKey publicKey = JWTConfiguration.loadPublicKey("certs/public-key.pem");

            // 3. Build the JWT Claims Set
            JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder()
                    .subject(String.valueOf(userDTO.getUsersId()))
                    .issuer(remoteIP)
                    .expirationTime(Date.from(EXPIRE_AT))
                    .issueTime(Date.from(ISSUED_AT))
                    .claim("AUTH_ACCESS", userDTO.getUsersId());


            Map<String, String> additionalClaims = getAdditionalClaims(userDTO);
            additionalClaims.forEach(claimsBuilder::claim);

            JWTClaimsSet jwtClaimsSet = claimsBuilder.build();

            // 4. Encrypt the JWT
            JWEHeader header = new JWEHeader.Builder(
                    JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256GCM
            ).contentType("JWT") // Optional header parameter for nested JWT
                    .build();

            EncryptedJWT encryptedJWT = new EncryptedJWT(header, jwtClaimsSet);

            // Perform encryption with the public key
            RSAEncrypter encrypter = new RSAEncrypter(publicKey);
            encryptedJWT.encrypt(encrypter);

            // 5. Serialize and return the JWT
            String jwtToken = encryptedJWT.serialize();

            log.info("[OauthJWTUtility:GENERATE_ACCESS_TOKEN] Token CREATION completed for:{}", userDTO.getUsersFullname());

            return jwtToken;
        } catch (JOSEException | GeneralSecurityException | IOException e) {
            log.error("[OauthJWTUtility:GENERATE_ACCESS_TOKEN] Token CREATION failed for:{}", userDTO.getUsersFullname());
            log.error("[OauthJWTUtility:GENERATE_ACCESS_TOKEN] Exception:{}", e.getMessage());
            throw new CoreThrowHandler(RestApiError.INTERNAL_SERVER_ERROR, null, messageSource);
        }
    }

    /**
     * Decrypts an OAuth access token (JWE) and returns the JWT subject (user identifier).
     *
     * @param serializedJwt compact serialized encrypted JWT from the client
     * @return subject claim, typically the authenticated user's id
     */
    public String resolveSubjectFromEncryptedToken(String serializedJwt) {
        try {
            RSAPrivateKey privateKey = JWTConfiguration.loadPrivateKey("certs/private-key.pem");
            EncryptedJWT encryptedJWT = EncryptedJWT.parse(serializedJwt);
            encryptedJWT.decrypt(new RSADecrypter(privateKey));
            String subject = encryptedJWT.getJWTClaimsSet().getSubject();
            if (subject == null || subject.isBlank()) {
                throw new CoreThrowHandler(RestApiError.UNAUTHORIZED, messageSource);
            }
            return subject;
        } catch (JOSEException | ParseException e) {
            log.warn("[OauthJWTUtility:RESOLVE_SUBJECT] Invalid or unreadable token: {}", e.getMessage());
            throw new CoreThrowHandler(RestApiError.UNAUTHORIZED, messageSource);
        }
    }

    private static Map<String, String> getAdditionalClaims(UserDTO userDTO) {
        Map<String, String> additionalClaims = new HashMap<>();
        additionalClaims.put("user_id", String.valueOf(userDTO.getUsersId()));
        additionalClaims.put("user_full_name", userDTO.getUsersFullname());
        additionalClaims.put("user_email", userDTO.getUsersEmail());

        return additionalClaims;
    }
}
