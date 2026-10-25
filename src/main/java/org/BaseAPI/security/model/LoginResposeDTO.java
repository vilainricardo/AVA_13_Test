package org.BaseAPI.security.model;

import lombok.Getter;
import lombok.Setter;
import org.BaseAPI.commons.users.dto.UserDTO;

@Getter
@Setter
public class LoginResposeDTO {
    private UserDTO user;
    private String token;
    private String refreshToken;

}
