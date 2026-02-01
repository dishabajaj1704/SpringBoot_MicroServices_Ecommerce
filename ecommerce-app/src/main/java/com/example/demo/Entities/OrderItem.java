package com.example.demo.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;

    @ManyToOne   //Many order items can refer to one product
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    private BigDecimal price;

    @ManyToOne   //Many order items can belong to one order. For example, a phone order can have multiple items like phone case, charger, etc.
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;


}
