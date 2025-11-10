package com.zosh.configrations;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class JwtProvider {

    static SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

    // ✅ Create Token
    public String generateToken(Authentication auth){
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        String roles = populateAuthorities(authorities);

        return Jwts.builder()
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + 86400000)) // 1 day
                .claim("email", auth.getName())
                .claim("authorities", roles)
                .signWith(key)
                .compact();
    }

    // ✅ Extract Email
    public String getEmailFromJwtToken(String jwt){
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;

        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(jwt)
                .getPayload();

        return claims.get("email", String.class);
    }

    // ✅ Check if token is valid (signature + structure)
    public boolean isTokenValid(String token) {
        try {
            token = token.startsWith("Bearer ") ? token.substring(7) : token;

            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);  // throws exception if invalid

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    // ✅ Check if token is expired
    public boolean isTokenExpired(String token) {
        try {
            token = token.startsWith("Bearer ") ? token.substring(7) : token;

            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.getExpiration().before(new Date());

        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    // ✅ Validate token + email match
    public boolean validateToken(String token, String email) {
        if (!isTokenValid(token)) return false;
        if (isTokenExpired(token)) return false;
        return getEmailFromJwtToken(token).equals(email);
    }

    // ✅ Convert roles to CSV string
    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> auths = new HashSet<>();

        for(GrantedAuthority authority : authorities){
            auths.add(authority.getAuthority());
        }
        return String.join(",", auths);
    }
}
