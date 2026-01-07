package org.BaseAPI.security.Services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class JwtAuthFilterService extends OncePerRequestFilter {

    private final AuthService authService;

    public JwtAuthFilterService(AuthService authService) {
        this.authService = authService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) {
        try {

            String path = request.getRequestURI();
            // Skip docs
            if (path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs")) {
                chain.doFilter(request, response);
                return;
            }


            String header = request.getHeader("Authorization");
            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7);

                // Validate and parse
                Jws<Claims> jws = authService.parse(token);
                Claims claims = jws.getBody();

                // Extract identity and optional roles
                String subject = claims.getSubject();          // your user UUID
                String email = claims.get("email", String.class);

                // If you store roles (e.g., ["ADMIN","USER"]) as a claim:
                @SuppressWarnings("unchecked")
                List<String> roles = (List<String>) claims.get("roles"); // may be null

                List<GrantedAuthority> authorities = roles.stream()
                        .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                        .collect(Collectors.toList());


                // Build Authentication (principal can be subject or a custom object)
                var authentication = new UsernamePasswordAuthenticationToken(
                        subject, // principal (you can use a custom UserDetails)
                        null,    // credentials not needed for JWT
                        authorities
                );

                // Optionally attach extra details (email) via request attribute:
                request.setAttribute("email", email);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            chain.doFilter(request, response);

        } catch (JwtException e) {
            // Signature invalid, token expired, malformed, etc.
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            // Optionally write a JSON error body:
            response.setContentType("application/json");
            try {
                response.getWriter().write("{\"error\":\"invalid_or_expired_token\"}");
            } catch (Exception ignored) {}
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }


}
