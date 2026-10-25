package org.BaseAPI.commons.users.controller;

import jakarta.validation.Valid;
import org.BaseAPI.commons.users.entity.UserHumor;
import org.BaseAPI.constants.Humors;
import org.BaseAPI.constants.UIRoles;
import org.BaseAPI.commons.users.dto.UserDTO;
import org.BaseAPI.commons.users.dto.CreateUserRequestDTO;
import org.BaseAPI.commons.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/user/add")
    public UserDTO add(@Valid @RequestBody CreateUserRequestDTO createUserRequestDTO) {
        return userService.add(createUserRequestDTO);
    }

    @PostMapping("/user/add")
    public UserDTO add(@Valid @RequestBody CreateUserRequestDTO createUserRequestDTO) {
        return userService.add(createUserRequestDTO);
    }
    @GetMapping("/user/get-all")
    public List<UserDTO> getUser() {
        return userService.getAll();
    }


    @PostMapping("/user/add-roles/{id}")
    public UserDTO addRoles(@RequestBody UIRoles[] roles, @PathVariable String id) {
        return userService.addUIRoles(roles, id);
    }

    @PostMapping("/user/add-humor/{userId}/{humor}")
    public UserHumor addHumor(@PathVariable String userId, Humors humor) throws Exception {
        return userService.addUserHumor(userId, humor);
    }

}
