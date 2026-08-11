package com.product.shoppapp.services;

import com.product.shoppapp.dtos.OrderDetailDTO;
import com.product.shoppapp.exceptions.DataNotFoundException;
import com.product.shoppapp.models.Order;
import com.product.shoppapp.models.OrderDetail;
import com.product.shoppapp.models.Product;
import com.product.shoppapp.repositories.OrderDetailRepository;
import com.product.shoppapp.repositories.OrderRepository;
import com.product.shoppapp.repositories.ProductRepository;
import com.product.shoppapp.responses.OrderDetailResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService{
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private  final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public OrderDetailResponse createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception {
        Order existingOrder = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Cannot found order by id " + orderDetailDTO.getOrderId()));
        Product existingProduct = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Cannot found product by id " + orderDetailDTO.getProductId()));
        OrderDetail orderDetail = OrderDetail.builder()
                .order(existingOrder)
                .product(existingProduct)
                .price(orderDetailDTO.getPrice())
                .numberOfProducts(orderDetailDTO.getNumberOfProduct())
                .color(orderDetailDTO.getColor())
                .totalMoney(orderDetailDTO.getTotalMoney())
                .build();
        return OrderDetailResponse.fromOrderDetail(orderDetailRepository.save(orderDetail));
    }

    @Override
    public OrderDetailResponse getOrderDetailById(Long id) throws DataNotFoundException {
        OrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("OrderDetail not found"));
        return OrderDetailResponse.fromOrderDetail(orderDetail);
    }

    @Override
    public OrderDetailResponse updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws Exception {
        OrderDetail existingOrderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot found order detail by id " + id));
        Order existingOrder = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Cannot found order by id " + orderDetailDTO.getOrderId()));
        Product existingProduct = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Cannot found product by id " + orderDetailDTO.getProductId()));
        existingOrderDetail.setOrder(existingOrder);
        existingOrderDetail.setProduct(existingProduct);
        existingOrderDetail.setPrice(orderDetailDTO.getPrice());
        existingOrderDetail.setNumberOfProducts(orderDetailDTO.getNumberOfProduct());
        existingOrderDetail.setColor(orderDetailDTO.getColor());
        existingOrderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
        return OrderDetailResponse.fromOrderDetail(orderDetailRepository.save(existingOrderDetail));
    }

    @Override
    public void deleteOrderDetail(Long id) throws Exception {
        OrderDetail orderDetail = (OrderDetail) orderDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find Order Detail by id " + id));
        orderDetailRepository.delete(orderDetail);
    }

    @Override
    public List<OrderDetailResponse> getOrderDetails(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId).stream().map(OrderDetailResponse::fromOrderDetail).toList();
    }
}
