package com.product.shoppapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {

    @JsonProperty("order_id")
    @Min(value = 1, message = "Order id must be greater than 0")
    private Long orderId;

    @JsonProperty("product_id")
    @Min(value = 1, message = "Product id must be greater than 0")
    private Long productId;

    @JsonProperty("number_of_product")
    @Min(value = 1, message = "Number of product must be greater than 0")
    private Long numberOfProduct;

    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private Float price;

    @JsonProperty("total_money")
    @Min(value = 0, message = "Total money must be greater than or equal to 0")
    private Float totalMoney;

    private String color;
}
