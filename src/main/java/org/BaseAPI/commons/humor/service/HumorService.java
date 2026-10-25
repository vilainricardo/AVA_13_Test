package org.BaseAPI.commons.humor.service;

import lombok.AllArgsConstructor;
import org.BaseAPI.commons.humor.dto.CreateHumorDTO;
import org.BaseAPI.commons.humor.entity.Humor;
import org.BaseAPI.commons.users.entity.User;
import org.BaseAPI.commons.users.service.UserService;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class HumorService {

    public final UserService userService;

    public Humor add(CreateHumorDTO dto){
        User user = userService.findUserById(dto.getUserUuid());
        Humor humor = new Humor(user, dto.getHumor());

    }
}
