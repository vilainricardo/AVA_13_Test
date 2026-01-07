package org.BaseAPI.generalHub.controller;

import jakarta.validation.Valid;
import org.BaseAPI.constants.UIRoles;
import org.BaseAPI.generalHub.dto.user.UserDTO;
import org.BaseAPI.generalHub.dto.user.CreateUserRequestDTO;
import org.BaseAPI.generalHub.service.UserService;
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

    @GetMapping("/user/{id}")
    public UserDTO getUser(@PathVariable String id) {
        return userService.findById(id);
    }

    @GetMapping("/user/get-all")
    public List<UserDTO> getUser() {
        return userService.getAll();
    }


    @PostMapping("/user/add-roles/{id}")
    public UserDTO addRoles(@RequestBody UIRoles[] roles, @PathVariable String id) {
        return userService.addUIRoles(roles, id);
    }

}
