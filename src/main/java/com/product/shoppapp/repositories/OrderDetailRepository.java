package com.product.shoppapp.repositories;

import com.product.shoppapp.models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    List<OrderDetail> findByOrder_Id(Long orderId);
}
