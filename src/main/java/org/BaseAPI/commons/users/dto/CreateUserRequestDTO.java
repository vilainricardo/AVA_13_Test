package org.BaseAPI.commons.users.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.BaseAPI.commons.users.entity.User;
import org.BaseAPI.utils.ReflectionMapper;

import java.time.LocalDate;

@Getter
@Setter
public class CreateUserRequestDTO implements BaseDTO<User> {

    @NotBlank
    @Size(max = 100)
    private String firstName;

    @NotBlank
    @Size(max = 100)
    private String lastName;

    @NotBlank
    @Email
    @Size(max = 255)
    private String email;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDate birthDay;

    @NotBlank
    @Size(min = 8, max = 128)
    private String password;

    @Override
    public User toModel() {
        return ReflectionMapper.map(this, User.class);
    }
}
