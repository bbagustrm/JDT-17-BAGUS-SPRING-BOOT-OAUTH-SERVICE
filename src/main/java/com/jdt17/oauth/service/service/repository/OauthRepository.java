package com.jdt17.oauth.service.service.repository;

import com.jdt17.oauth.service.data.module.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OauthRepository extends JpaRepository<UserDTO, UUID> {

    Optional<UserDTO> getByUsersEmail(String email);
    Optional<UserDTO> getByUsersId(UUID id);
}
