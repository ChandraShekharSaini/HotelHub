package com.spring.authservice.jwtUtils;

import com.spring.authservice.entity.User;
import com.spring.authservice.service.CustomUserDetails;
import com.spring.authservice.service.UserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtUtils {

    @Autowired
    UserDetailsService userDetailsService;

    @Value("${spring.jwt.ExpirationTimeInMille}")
    private Integer ExpirationTimeInMille;

    @Value("${spring.jwt.JWT_SECREAT_KEY}")
    private String SECREAT_KEY;

    private static final Logger logger = (Logger) LoggerFactory.getLogger(JwtUtils.class);

    //1. Extract Jwt token from request
    public String getJwtTokenFromHeader(HttpServletRequest req) {
        String jwtString = req.getHeader("Authorization");
        if (jwtString != null && jwtString.startsWith("Bearer ")) {
            return jwtString.substring(7);
        }

        return null;
    }

    public Key key() {
        return Keys.hmacShaKeyFor(SECREAT_KEY.getBytes());
    }

    //2. Generate Jwt Token
    public String generateJwtToken(CustomUserDetails userDetails) {

        return Jwts.builder()
                .setSubject(userDetails.getEmail())
                .claim("userId", userDetails.getId())
                .claim("firstName", userDetails.getFirstName())
                .claim("lastName", userDetails.getLastName())
                .claim("userEmail", userDetails.getEmail())
                .claim("userRole", userDetails.getAuthorities()
                )
                .setIssuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ExpirationTimeInMille))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();

    }

    //3. Getting Username From  Username
    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload().getSubject();
    }

    //4. Validating Jwt Token\
    public boolean ValidateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parseSignedClaims(authToken);

            return true;

        } catch (MalformedJwtException e) {
            logger.error("Invalid Jwt Token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Jwt token is Unsupported: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Expired Jwt Token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Jwt claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}