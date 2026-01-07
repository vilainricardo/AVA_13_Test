package org.BaseAPI.erros;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiError {
    private String type;        // URI identifier of the problem (optional, RFC 7807)
    private String title;       // short, human-readable summary
    private int status;         // HTTP status code
    private String detail;      // detailed message (safe for clients)
    private String instance;    // URI of the request (optional)

}

