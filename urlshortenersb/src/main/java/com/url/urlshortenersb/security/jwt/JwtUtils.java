package com.url.urlshortenersb.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import com.url.urlshortenersb.service.UserDetailsImpl;
import java.util.stream.Collectors;
import java.util.Date;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import java.security.Key;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import io.jsonwebtoken.JwtException;

@Component
public class JwtUtils {
    //Authorization -> Bearer <token>
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;

    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String generateToken(UserDetailsImpl userDetails) {
        // Implementation for generating JWT token based on user details
        // This is a placeholder, actual implementation will depend on your JWT library
        String username = userDetails.getUsername();
        String roles = userDetails.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.joining(","));
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date((new Date().getTime() + jwtExpirationMs))) // 1 day expiration
                .signWith(key())
                .compact();
    }

    public String getUserNameFromJwt(String token) {
        return Jwts.parser()
                .setSigningKey(key())
                .verifyWith((SecretKey) key())
                .build().parseSignedClaims(token)
                .getPayload().getSubject();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateToken(String authToken){
        try {
            Jwts.parser()
                .verifyWith((SecretKey) key())
                .build().parseSignedClaims(authToken);
            return true;
        } catch (JwtException e) {
            throw new RuntimeException(e);
        } catch(IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
