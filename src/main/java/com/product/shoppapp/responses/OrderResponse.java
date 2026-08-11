package com.product.shoppapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.product.shoppapp.models.Order;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long id;

    @JsonProperty("full_name")
    private String fullName;

    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;
    private String address;
    private String note;

    @JsonProperty("order_date")
    private LocalDateTime orderDate;

    private String status;

    @JsonProperty("total_money")
    private Float totalMoney;

    @JsonProperty("shipping_method")
    private String shippingMethod;

    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("shipping_date")
    private LocalDate shippingDate;

    @JsonProperty("tracking_method")
    private String trackingMethod;

    @JsonProperty("payment_method")
    private String paymentMethod;

    private Boolean active;

    @JsonProperty("user_id")
    private Long userId;
}
