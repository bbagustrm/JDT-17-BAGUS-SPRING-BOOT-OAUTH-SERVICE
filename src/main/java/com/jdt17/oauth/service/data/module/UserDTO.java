package com.jdt17.oauth.service.data.module;

import com.jdt17.oauth.service.data.utility.table.MtUsersVariable;
import com.jdt17.oauth.service.data.utility.table.schema.SchemaTableVariable;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = MtUsersVariable.TABLE_MT_USERS, schema = SchemaTableVariable.SCHEMA_PUBLIC)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = MtUsersVariable.COLUMN_MT_USERS_ID, nullable = false)
    private UUID usersId;

    @Column(name = MtUsersVariable.COLUMN_MT_USERS_FULL_NAME)
    private String usersFullname;

    @Column(name = MtUsersVariable.COLUMN_MT_USERS_CREDENTIALS)
    private String usersCredentials;

    @Column(name = MtUsersVariable.COLUMN_MT_USERS_EMAIL)
    private String usersEmail;

    @Column(name = "users_device")
    private String usersDevice;

    @Column(name = "users_status", nullable = false)
    private Boolean usersStatus = true;

    @Column(name = "users_created_date", nullable = false)
    private LocalDateTime usersCreatedDate;

    @Column(name = "users_updated_date")
    private LocalDateTime usersUpdatedDate;

}
