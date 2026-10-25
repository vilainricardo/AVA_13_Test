package org.BaseAPI.security.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.BaseAPI.commons.users.entity.User;
import org.BaseAPI.utils.UUID.UUIDHandler;

import java.util.Date;

@Entity
@Table(
        name = "refresh_tokens",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_refresh_token_user", columnNames = {"user_uuid"})
        }
)
@Getter
@Setter
public class RefreshToken {

    @Id
    @Column(nullable = false, updatable = false)
    private String uuid = UUIDHandler.generateId();

    @OneToOne(optional = false)
    @JoinColumn(name = "user_uuid", referencedColumnName = "uuid", nullable = false)
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date expiresAt;

    @Column(nullable = false)
    private boolean revoked;

    @PrePersist
    public void prePersist() {
        if (uuid == null || uuid.isBlank()) {
            uuid = UUIDHandler.generateId();
        }
    }
}
