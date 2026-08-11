package com.product.shoppapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.product.shoppapp.models.OrderDetail;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResponse {

    private Long id;

    @JsonProperty("order_id")
    private Long orderId;

    @JsonProperty("product_id")
    private Long productId;


    @JsonProperty("number_of_product")
    private Long numberOfProduct;


    private Float price;


    @JsonProperty("total_money")
    private Float totalMoney;

    private String color;


    public static OrderDetailResponse fromOrderDetail(OrderDetail orderDetail){
        return OrderDetailResponse.builder()
                .id(orderDetail.getId())
                .orderId(orderDetail.getOrder().getId())
                .productId(orderDetail.getProduct().getId())
                .numberOfProduct(orderDetail.getNumberOfProducts())
                .price(orderDetail.getProduct().getPrice())
                .totalMoney(orderDetail.getTotalMoney())
                .color(orderDetail.getColor())
                .build();
    }
}
