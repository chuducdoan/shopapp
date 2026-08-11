package com.product.shoppapp.services;

import com.product.shoppapp.dtos.OrderDetailDTO;
import com.product.shoppapp.exceptions.DataNotFoundException;
import com.product.shoppapp.models.OrderDetail;
import com.product.shoppapp.responses.OrderDetailResponse;

import java.util.List;

public interface IOrderDetailService {
    OrderDetailResponse createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception;

    OrderDetailResponse getOrderDetailById(Long id) throws DataNotFoundException;

    OrderDetailResponse updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws Exception;

    void deleteOrderDetail(Long id) throws Exception;

    List<OrderDetailResponse> getOrderDetails(Long orderId);
}
