package com.product.shoppapp.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "tokens")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Token {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false, length = 255, unique = true)
    private String token;

    @Column(name = "token_type", length = 255)
    private String tokenType;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;


    private Boolean revoked;

    private Boolean expired;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
