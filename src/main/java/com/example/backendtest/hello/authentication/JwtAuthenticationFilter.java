package com.example.backendtest.hello.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private String signingKey;

    public JwtAuthenticationFilter(String signingKey) {
        this.signingKey = signingKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String authorization = request.getHeader("Authorization");

        try {
            String token = extractTokenFromHeader(authorization);

            if (token != null) {
                log.info(token);
                SecurityContextHolder.getContext().setAuthentication(new JwtAuthentication(usernameFromToken(token)));
            }
        } catch (Exception e) {
            log.warn("unable to parse token", e);
        }

        chain.doFilter(request, response);
    }

    private String usernameFromToken(String token) {
        Jws<Claims> claimsJws = Jwts.parser()
                .setSigningKey(signingKey.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token);

        return claimsJws.getBody().get("username", String.class);
    }

    private String extractTokenFromHeader(String authorization) {
        if (authorization == null) {
            return null;
        }

        String[] headerParts = authorization.split(" ");

        if (headerParts.length != 2) {
            return null;
        }

        if (!headerParts[0].toLowerCase().equals("bearer")) {
            return null;
        }

        return headerParts[1];
    }
}

