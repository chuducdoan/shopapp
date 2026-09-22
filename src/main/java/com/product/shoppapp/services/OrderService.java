package com.product.shoppapp.services;

import com.product.shoppapp.dtos.CartItemDTO;
import com.product.shoppapp.dtos.OrderDTO;
import com.product.shoppapp.exceptions.DataNotFoundException;
import com.product.shoppapp.models.*;
import com.product.shoppapp.repositories.OrderDetailRepository;
import com.product.shoppapp.repositories.OrderRepository;
import com.product.shoppapp.repositories.ProductRepository;
import com.product.shoppapp.repositories.UserRepository;
import com.product.shoppapp.responses.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private  final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderDTO orderDTO) throws Exception {
        User existingUser = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find user with id =: " + orderDTO.getUserId()));

       modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
       Order order = new Order();
       modelMapper.map(orderDTO, order);
       order.setUser(existingUser);
       order.setOrderDate(new Date());
       order.setStatus(OrderStatus.PENDING);
       LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
       if (shippingDate.isBefore(LocalDate.now())) {
           throw  new DataNotFoundException("Shipping date must be in the future");
       }
       order.setActive(true);
       order.setShippingDate(shippingDate);
       order.setTotalMoney(orderDTO.getTotalMoney());
       orderRepository.save(order);

       // Tạo danh sách các đối tượng OrderDetail từ cartItems
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (CartItemDTO cartItemDTO : orderDTO.getCartItems()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            Long productId = cartItemDTO.getProductId();
            Long quantity = cartItemDTO.getQuantity();
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new DataNotFoundException("Cannot found product with id =: " + productId));
            orderDetail.setProduct(product);
            orderDetail.setNumberOfProducts(quantity);
            orderDetail.setPrice(product.getPrice());
            orderDetails.add(orderDetail);
        }
        orderDetailRepository.saveAll(orderDetails);
       return OrderResponse.fromOrder(order);
    }

    @Override
    public OrderResponse getOrder(Long id) throws DataNotFoundException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot found order"));
        return modelMapper.map(order, OrderResponse.class);
    }

    @Override
    public OrderResponse updateOrder(Long id, OrderDTO orderDTO) throws Exception {
        Order order = orderRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Cannot found order"));
        User existingUser = userRepository.findById(orderDTO.getUserId()).orElseThrow(() -> new DataNotFoundException("Cannot found user"));
        modelMapper.typeMap(OrderDTO.class, Order.class).addMappings(mapper -> mapper.skip(Order::setId));
        modelMapper.map(orderDTO, order);
        order.setUser(existingUser);
        return modelMapper.map(orderRepository.save(order), OrderResponse.class);
    }

    @Override
    public void deleteOrder(Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            optionalOrder.get().setActive(false);
            orderRepository.save(optionalOrder.get());
        }
    }

    @Override
    public List<OrderResponse> findByUserId(Long userId) {
        return orderRepository.findByUserId(userId)
                .stream().map(order -> modelMapper.map(order, OrderResponse.class)).toList();
    }
}
