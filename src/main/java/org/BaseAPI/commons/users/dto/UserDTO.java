package org.BaseAPI.commons.users.dto;

import lombok.Getter;
import lombok.Setter;
import org.BaseAPI.constants.UIRoles;
import org.BaseAPI.commons.users.entity.User;
import org.BaseAPI.utils.ReflectionMapper;

import java.time.LocalDate;
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
