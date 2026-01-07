
package org.BaseAPI.security.configs;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Base64;

@Configuration
public class JwtConfig {

    @Value("${security.jwt.secret-base64}")
    private String secretBase64;

    @Value("${security.jwt.expiration-min}")
    private long expirationMin;

    @Bean
    public SecretKey jwtSecretKey() {
        if (secretBase64 == null || secretBase64.isBlank()) {
            throw new IllegalStateException(
                    "Missing property 'security.jwt.secret-base64'. " +
                            "Provide a Base64-encoded secret (e.g., env var JWT_SECRET_BASE64)."
            );
        }
        byte[] keyBytes;
        try {

            keyBytes = Base64.getDecoder().decode(secretBase64);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(
                    "Property 'security.jwt.secret-base64' is not valid Base64. " +
                            "Generate with: `openssl rand -base64 32`.", e
            );
        }
        // HS256 requires at least 256-bit (32 bytes). Keys.hmacShaKeyFor() will also validate.
        if (keyBytes.length < 32) {
            throw new IllegalStateException(
                    "JWT secret too short. Provide at least 32 bytes (256 bits) before Base64 encoding."
            );
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Bean
    public Duration jwtTokenExpiration() {
        if (expirationMin <= 0) {
            // Default to 60 minutes if misconfigured
            return Duration.ofMinutes(60);
        }
        return Duration.ofMinutes(expirationMin);
    }

    @Bean
    public Duration jwtRefreshTokenExpiration() {
        if (expirationMin <= 0) {
            // Default to 60 minutes if misconfigured
            return Duration.ofDays(10);
        }
        return Duration.ofMinutes(expirationMin);
    }
}
