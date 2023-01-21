package com.boiis.todo.helper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
public class JwtToken {
    @Value("${jwt.secret}")
    private String secret;

    // 1 day
    private long validation = 24 * 3600;
    public String generateJwtToken(String userId){
        return Jwts.builder().setIssuedAt(new Date(System.currentTimeMillis())).setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis() + validation * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public String getUserIdFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    //for retrieveing any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    //check if the token has expired
    public Boolean isTokenExpired(String token) {
        try {
            final Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        }
        catch(Exception e)
        {
            return true;
        }

    }
}
