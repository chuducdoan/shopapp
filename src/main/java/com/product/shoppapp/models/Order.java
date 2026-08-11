package com.product.shoppapp.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "orders")
@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fullname", length = 100)
    private String fullName;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
    private String address;
    private String note;

    @Column(name = "order_date")
    private Date orderDate;

    private String status;

    @Column(name = "total_money")
    private Float totalMoney;

    @Column(name = "shipping_method")
    private String shippingMethod;

    @Column(name = "shipping_address")
    private String shippingAddress;

    @Column(name = "shipping_date")
    private LocalDate shippingDate;

    @Column(name = "tracking_method")
    private String trackingMethod;

    @Column(name = "payment_method")
    private String paymentMethod;

    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
