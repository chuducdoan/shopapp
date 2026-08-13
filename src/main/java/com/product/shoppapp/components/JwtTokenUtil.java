package com.product.shoppapp.components;

import com.product.shoppapp.exceptions.InvalidParamException;
import com.product.shoppapp.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import io.jsonwebtoken.io.Decoders;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    @Value("${jwt.expiration}")
    private int expiration;

    @Value("${jwt.secretKey}")
    private String secretKey;

    public String generateToken(User user) throws Exception {
        // properties => claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("phoneNumber", user.getPhoneNumber());
        try {
           String token = Jwts.builder()
                   .claims(claims)
                   .subject(user.getPhoneNumber())
                   .expiration(new Date(System.currentTimeMillis() + expiration * 1000L))
                   .signWith(getSignInKey())
                   .compact();
            return token;
        } catch (Exception e) {
            throw  new InvalidParamException("Cannot create jwt token, error" + e.getMessage());
        }
    }

    private SecretKey getSignInKey() {
        byte[] bytes = Decoders.BASE64.decode(secretKey.trim());
        return Keys.hmacShaKeyFor(bytes);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey()) // Hoặc .setSigningKey(...) nếu dùng bản cũ hơn
                .build()
                .parseSignedClaims(token)
                .getPayload(); // Thay cho .getBody()
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // check expiration
    public Boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public String extractPhoneNumber(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String phoneNumber = extractPhoneNumber(token);
        return (phoneNumber.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
