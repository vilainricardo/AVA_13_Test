package org.BaseAPI.commons.users.service;

import org.BaseAPI.commons.users.entity.UserHumor;
import org.BaseAPI.constants.Humors;
import org.BaseAPI.constants.SecurityRoles;
import org.BaseAPI.constants.UIRoles;
import org.BaseAPI.erros.exceptions.ResourceNotFoundException;
import org.BaseAPI.commons.users.dto.UserDTO;
import org.BaseAPI.commons.users.dto.CreateUserRequestDTO;
import org.BaseAPI.commons.users.entity.User;
import org.BaseAPI.commons.users.repository.UserRepository;
import org.BaseAPI.security.Services.PasswordService;
import org.BaseAPI.utils.UUID.UUIDHandler;
import org.BaseAPI.utils.fileUtils.WriteFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public UserDTO addUIRoles(UIRoles[] roles, String uuid) {
        User user = findUserById(uuid);
        for(UIRoles role:roles){
            user.getUiRoles().add(role);
        }
        user = userRepository.save(user);
        return user.toDto();
    }

    public UserHumor addUserHumor(String uuid, Humors humor) throws Exception {
        User user = findUserById(uuid);
        UserHumor userHumor = new UserHumor();
        userHumor.setHumor(humor);
        userHumor.setUser(user);


        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String actualDate = LocalDateTime.now().format(dateFormat);

        String line = "%s;%s;%s".formatted(actualDate, userHumor.getUser().getUuid(),userHumor.getHumor());

        WriteFile writeFile = new WriteFile();
        writeFile.writeFile( "humor", "%s.txt".formatted(userHumor.getUser().getUuid()),line, false );

        return userHumor;
    }
}
