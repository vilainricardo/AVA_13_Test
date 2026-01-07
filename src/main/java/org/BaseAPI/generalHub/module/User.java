package org.BaseAPI.generalHub.module;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.BaseAPI.constants.SecurityRoles;
import org.BaseAPI.constants.UIRoles;
import org.BaseAPI.generalHub.dto.user.UserDTO;
import org.BaseAPI.utils.ReflectionMapper;
import org.BaseAPI.utils.UUID.UUIDHandler;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User implements BaseModel<UserDTO> {

    @Id
    @NotBlank
    @Column(nullable = false)
    private String uuid = UUIDHandler.generateId();
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
    @NotBlank
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
