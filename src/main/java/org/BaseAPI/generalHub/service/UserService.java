package org.BaseAPI.generalHub.service;

import org.BaseAPI.constants.SecurityRoles;
import org.BaseAPI.constants.UIRoles;
import org.BaseAPI.erros.exceptions.ResourceNotFoundException;
import org.BaseAPI.generalHub.dto.user.UserDTO;
import org.BaseAPI.generalHub.dto.user.CreateUserRequestDTO;
import org.BaseAPI.generalHub.module.User;
import org.BaseAPI.generalHub.repository.UserRepository;
import org.BaseAPI.security.Services.PasswordService;
import org.BaseAPI.utils.UUID.UUIDHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;


    @Autowired
    PasswordService passwordService;


    public UserDTO add(CreateUserRequestDTO createUserRequestDTO) {

        User user = createUserRequestDTO.toModel();
        user.setUuid(UUIDHandler.generateId());
        user.setPassword(passwordService.hashPassword(createUserRequestDTO.getPassword()));
        user.setUiRoles(Set.of(UIRoles.G_USER));
        user.setSecurityRoles(Set.of(SecurityRoles.BASIC));

        user = userRepository.save(user);
        return user.toDto();
    }

    /**
     * Find a specific User by UUID
     * @param uuid - UUID for this user
     * @return The found User
     */
    public UserDTO findById(String uuid) {
        return findUserById(uuid).toDto();
    }

    public User findUserById(String uuid){
        User user = userRepository.findByUuid(uuid).orElse(null);
        if(user == null){
            throw new ResourceNotFoundException("User not found");
        }
        return user;
    }

    public User findUserByEmail(String email){
        User user = userRepository.findByEmail(email).orElse(null);
        if(user == null){
            throw new ResourceNotFoundException("User not found");
        }
        return user;
    }

    public List<UserDTO> getAll() {
        List<User> users = userRepository.findAll();

        return users.stream().map(User::toDto).toList();
    }

    public UserDTO addUIRoles(UIRoles[] roles, String id) {
        User user = findUserById(id);
        for(UIRoles role:roles){
            user.getUiRoles().add(role);
        }
        user = userRepository.save(user);
        return user.toDto();
    }

}
