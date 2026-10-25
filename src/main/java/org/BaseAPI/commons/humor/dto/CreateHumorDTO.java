package org.BaseAPI.commons.humor.dto;

import lombok.Getter;
import lombok.Setter;
import org.BaseAPI.constants.Humors;

@Getter
@Setter
public class CreateHumorDTO {
    private Humors humor;
    private String userUuid;
}
