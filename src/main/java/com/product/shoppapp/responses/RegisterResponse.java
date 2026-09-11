package com.product.shoppapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.product.shoppapp.models.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResponse {

    @JsonProperty("message")
    private String message;

    @JsonProperty("user")
    private User user;
}
