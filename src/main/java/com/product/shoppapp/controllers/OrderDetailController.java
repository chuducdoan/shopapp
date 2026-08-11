package com.product.shoppapp.controllers;

import com.product.shoppapp.dtos.OrderDetailDTO;
import com.product.shoppapp.exceptions.DataNotFoundException;
import com.product.shoppapp.models.OrderDetail;
import com.product.shoppapp.responses.OrderDetailResponse;
import com.product.shoppapp.services.IOrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_details")
@RequiredArgsConstructor
public class OrderDetailController {
    private final IOrderDetailService orderDetailService;

    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            OrderDetailResponse orderDetail = orderDetailService.createOrderDetail(orderDetailDTO);
            return ResponseEntity.ok(orderDetail);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("id") Long id) {
        try {
            OrderDetailResponse orderDetail = orderDetailService.getOrderDetailById(id);
            return ResponseEntity.ok(orderDetail);
        } catch (DataNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetailsByOrderId(@Valid @PathVariable("orderId") Long orderId) {
        List<OrderDetailResponse> orderDetails = orderDetailService.getOrderDetails(orderId);
        return ResponseEntity.ok(orderDetails);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(@Valid @PathVariable("id") Long id,
                                              @Valid @RequestBody OrderDetailDTO orderDetailDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            OrderDetailResponse orderDetail = orderDetailService.updateOrderDetail(id, orderDetailDTO);
            return ResponseEntity.ok(orderDetail);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
     public ResponseEntity<?> deleteOrderDetail(@Valid @PathVariable("id") Long id) {
        try {
            orderDetailService.deleteOrderDetail(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("Delete order detail " + id + " successfully");
     }
}
