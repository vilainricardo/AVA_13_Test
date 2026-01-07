package org.BaseAPI.erros.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // optional if you always handle in advice
    public class ExpiredSessionException extends RuntimeException {

    static final String EXPIRED_MSG = "The session expired.";
        public ExpiredSessionException() {
            super(EXPIRED_MSG);
        }
    }


