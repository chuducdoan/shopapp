package com.product.shoppapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CartItemDTO {

    @JsonProperty("product_id")
    private Long productId;

    private Long quantity;
}
