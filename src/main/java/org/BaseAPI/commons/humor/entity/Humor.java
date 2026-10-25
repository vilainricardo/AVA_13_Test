package org.BaseAPI.commons.humor.entity;

import lombok.Getter;
import lombok.Setter;
import org.BaseAPI.commons.users.entity.User;
import org.BaseAPI.constants.Humors;

import java.time.LocalDateTime;

@Setter
@Getter
public class Humor {
    private long id;
    private Humors humor;
    private LocalDateTime humorAt;
    private User user;

    public Humor(User user, Humors humor){
        this.humorAt = LocalDateTime.now();
        this.user = user;
        this.humor = humor;
    }
}
