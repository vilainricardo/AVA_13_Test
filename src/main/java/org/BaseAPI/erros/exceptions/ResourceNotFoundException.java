package org.BaseAPI.erros.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // optional if you always handle in advice
    public class ResourceNotFoundException extends RuntimeException {
//        public ResourceNotFoundException(String resourceName, Object id) {
//            super(resourceName + " not found with id: " + id);
//        }

        public ResourceNotFoundException(String message) {
            super(message);
        }
    }


