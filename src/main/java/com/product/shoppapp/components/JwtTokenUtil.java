package com.product.shoppapp.components;

import com.product.shoppapp.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    @Value("${jwt.expiration}")
    private int expiration;

    public String generateToken(User user) {

    }
}
