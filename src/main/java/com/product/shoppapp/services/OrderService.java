package com.product.shoppapp.services;

import com.product.shoppapp.dtos.OrderDTO;
import com.product.shoppapp.exceptions.DataNotFoundException;
import com.product.shoppapp.models.Order;
import com.product.shoppapp.models.OrderStatus;
import com.product.shoppapp.models.User;
import com.product.shoppapp.repositories.OrderRepository;
import com.product.shoppapp.repositories.UserRepository;
import com.product.shoppapp.responses.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private  final ModelMapper modelMapper;

    @Override
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
       orderRepository.save(order);
       return modelMapper.map(order, OrderResponse.class);
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
