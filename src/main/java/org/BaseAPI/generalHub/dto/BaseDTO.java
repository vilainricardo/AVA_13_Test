package org.BaseAPI.generalHub.dto;

import org.BaseAPI.generalHub.module.User;

public interface BaseDTO<D> {
    D toModel();
}
