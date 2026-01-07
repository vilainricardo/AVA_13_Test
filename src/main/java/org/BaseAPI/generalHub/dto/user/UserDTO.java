package org.BaseAPI.generalHub.dto.user;

import lombok.Getter;
import lombok.Setter;
import org.BaseAPI.constants.UIRoles;
import org.BaseAPI.generalHub.dto.BaseDTO;
import org.BaseAPI.generalHub.module.User;
import org.BaseAPI.utils.ReflectionMapper;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class UserDTO implements BaseDTO<User> {

    private String uuid;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDay;
    private Set<UIRoles> uiRoles;

    @Override
    public User toModel() {
        return ReflectionMapper.map(this, User.class);
    }
}
