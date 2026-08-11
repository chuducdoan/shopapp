package com.product.shoppapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageDTO {

    @JsonProperty("product_id")
    private Long productId;

    @JsonProperty("image_url")
    @Size(min = 1, max = 200, message = "Image URL is required")
    private String imageUrl;
}
