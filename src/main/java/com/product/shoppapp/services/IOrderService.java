package com.product.shoppapp.services;

import com.product.shoppapp.dtos.OrderDTO;
import com.product.shoppapp.exceptions.DataNotFoundException;
import com.product.shoppapp.models.Order;
import com.product.shoppapp.responses.OrderResponse;

import java.util.List;

public interface IOrderService {
    OrderResponse createOrder(OrderDTO orderDTO) throws Exception;

    OrderResponse getOrder(Long id) throws DataNotFoundException;

    OrderResponse updateOrder(Long id, OrderDTO orderDTO) throws Exception;

    void deleteOrder(Long id);

    List<OrderResponse> findByUserId(Long userId);
}
