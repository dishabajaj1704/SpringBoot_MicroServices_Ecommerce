package com.example.demo.Repositories;

import com.example.demo.DTOS.CartItemResponse;
import com.example.demo.Entities.CartItem;
import com.example.demo.Entities.Product;
import com.example.demo.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Integer> {

    // Custom query method to find CartItem by User and Product
    CartItem findByUserAndProduct(User user, Product product);

    void deleteByUserAndProduct(User user, Product product);

    List<CartItem> findByUser(User user);

    void deleteByUser(User user);
}
