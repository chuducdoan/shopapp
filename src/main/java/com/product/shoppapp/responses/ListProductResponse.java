package com.product.shoppapp.responses;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListProductResponse {
    List<ProductResponse> products;
    int totalPages;
}
