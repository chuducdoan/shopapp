package com.product.shoppapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.product.shoppapp.models.Role;
import com.product.shoppapp.models.User;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Long id;

    @JsonProperty("phone_number")
    private String phoneNumber;
    private String address;

    @JsonProperty("is_active")
    private boolean isActive;

    @JsonProperty("date_of_birth")
    private Date dateOfBirth;

    @JsonProperty("facebook_account_id")
    private int facebookAccountId;

    @JsonProperty("google_account_id")
    private int googleAccountId;
    private Role role;

    @JsonProperty("full_name")
    private String fullName;

    public static UserResponse fromUser(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .isActive(user.isActive())
                .dateOfBirth(user.getDateOfBirth())
                .facebookAccountId(user.getFacebookAccountId())
                .googleAccountId(user.getGoogleAccountId())
                .role(user.getRole())
                .fullName(user.getFullName())
                .build();
    }
}
