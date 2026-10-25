package org.BaseAPI.commons.users.entity;

import lombok.Getter;
import lombok.Setter;
import org.BaseAPI.constants.Humors;

@Getter
@Setter
public class UserHumor {
    private User user;
    private Humors humor;
}
