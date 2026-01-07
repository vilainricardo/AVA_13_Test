package org.BaseAPI.security.Services;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {


    private final Argon2PasswordEncoder encoder =
            new Argon2PasswordEncoder(16, 32, 1, 65536, 3); // 16B salt, 32B hash, 64MB, 3 iterations

    public String hashPassword(String plain) {
        return encoder.encode(plain); // salt is generated & embedded
    }

    public boolean verify(String plain, String storedHash) {
        return encoder.matches(plain, storedHash);
    }

}
