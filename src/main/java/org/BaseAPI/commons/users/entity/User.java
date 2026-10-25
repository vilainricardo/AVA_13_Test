package org.BaseAPI.commons.users.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.BaseAPI.constants.SecurityRoles;
import org.BaseAPI.constants.UIRoles;
import org.BaseAPI.commons.users.dto.UserDTO;
import org.BaseAPI.utils.ReflectionMapper;
import org.BaseAPI.utils.UUID.UUIDHandler;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User implements BaseEntity<UserDTO> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @GeneratedValue(generator = "uuid")
    @UuidGenerator()
    private String uuid;
    @NotBlank
    @Column(nullable = false)
    private String firstName;
    @NotBlank
    @Column(nullable = false)
    private String lastName;
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;
    @NotBlank
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDate birthDay;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_ui_roles",
            joinColumns = @JoinColumn(name = "user_uuid")
    )
    @Column(name = "ui_roles", nullable = false)
    private Set<UIRoles> uiRoles;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_security_roles",
            joinColumns = @JoinColumn(name = "user_uuid")
    )
    @Column(name = "security_roles", nullable = false)
    private Set<SecurityRoles> securityRoles;

    @Override
    public UserDTO toDto() {
        return ReflectionMapper.map(this, UserDTO.class);
    }
}
