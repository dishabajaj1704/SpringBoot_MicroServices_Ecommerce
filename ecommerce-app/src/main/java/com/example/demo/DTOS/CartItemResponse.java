package com.example.demo.DTOS;

import com.example.demo.Entities.Product;
import com.example.demo.Entities.User;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class CartItemResponse {

    private Product product;
    private User user;
    private Integer quantity;
    private BigDecimal price;
}
