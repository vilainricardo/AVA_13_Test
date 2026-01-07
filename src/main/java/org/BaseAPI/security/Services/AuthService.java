package org.BaseAPI.security.Services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.BaseAPI.constants.SecurityRoles;
import org.BaseAPI.erros.exceptions.ExpiredSessionException;
import org.BaseAPI.erros.exceptions.ResourceNotFoundException;
import org.BaseAPI.security.model.LoginResposeDTO;
import org.BaseAPI.generalHub.module.User;
import org.BaseAPI.generalHub.service.UserService;
import org.BaseAPI.security.model.RefreshToken;
import org.BaseAPI.security.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class AuthService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    UserService userService;
    @Autowired
    PasswordService passwordService;


    private final SecretKey secretKey;
    private final Duration tokenExpiration;
    private final Duration refreshTokenExpiration;

    public AuthService(SecretKey secretKey, Duration jwtTokenExpiration, Duration jwtRefreshTokenExpiration ) {
        this.secretKey = secretKey;
        this.tokenExpiration = jwtTokenExpiration;
        this.refreshTokenExpiration = jwtRefreshTokenExpiration;
    }

    /**
     * Log In The user
     * @param email - User email
     * @param password - User plain Password
     * @return - An exception or the found user
     */
    public LoginResposeDTO login(String email, String password) {
        LoginResposeDTO loginResposeDTO = new LoginResposeDTO();
        User user = userService.findUserByEmail(email);
        if(user == null || !passwordService.verify(password, user.getPassword())){
            throw new ResourceNotFoundException("User not found");
        }

        loginResposeDTO.setUser(user.toDto());
        loginResposeDTO.setRefreshToken(generateRefreshToken(user));
        loginResposeDTO.setToken(generateToken(user));





        return loginResposeDTO;
    }


    public String refreshToken(String refreshTokenValue) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenValue).orElse(null);
        String email = getEmailFromToken(refreshTokenValue);

        if(refreshToken == null || !email.equals(refreshToken.getUser().getEmail())){
            throw new ExpiredSessionException();
        }

        return generateToken(refreshToken.getUser());
    }


    private String generateRefreshToken(User user){
        Date expireDate = Date.from(Instant.now().plus(refreshTokenExpiration));

        String newToken = generateToken(user, expireDate);
        RefreshToken refreshToken = refreshTokenRepository.findByUserUuid(user.getUuid()).orElse(null);
        if(refreshToken == null){
            refreshToken = new RefreshToken();
            refreshToken.setExpiresAt(expireDate);
            refreshToken.setRevoked(false);
            refreshToken.setUser(user);
        }
        refreshToken.setToken(newToken);
        refreshTokenRepository.save(refreshToken);
        return newToken;
    }

    private String generateToken(User user){
        Date expireDate = Date.from(Instant.now().plus(tokenExpiration));
        return generateToken(user, expireDate);
    }

    private String generateToken(User user, Date expireDate){
        Set<SecurityRoles> roles = user.getSecurityRoles();
        return Jwts.builder()
                .setSubject(user.getUuid())
                .claim("email", user.getEmail())
                .claim("roles", roles == null ? List.of() : roles)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(expireDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parse(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwt); // throws if invalid/expired
    }

    public String getEmailFromToken(String jwt) {
        Jws<Claims> jws = parse(jwt);
        Claims claims = jws.getBody();
        return claims.get("email", String.class);
    }
}
