package com.example.demo.Services.UserService;


import com.example.demo.DTOS.CartItemRequest;
import com.example.demo.DTOS.CartItemResponse;
import com.example.demo.Entities.CartItem;
import com.example.demo.Entities.Product;
import com.example.demo.Entities.User;
import com.example.demo.Repositories.CartItemRepository;
import com.example.demo.Repositories.ProductRepository;
import com.example.demo.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional  // Ensures that all public methods are wrapped in a transaction. If any method throws an exception, the transaction will be rolled back. Why we need it here? Because we are performing multiple database operations that need to be atomic.
public class CartService {

    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    public boolean addToCart(String userId, CartItemRequest cartItemRequest) {
        // Look for Product
        Optional<Product> productOptional = productRepository.findById(cartItemRequest.getProductId());
        if (productOptional.isEmpty()) { return false;     }

        Product product = productOptional.get();
        if(product.getStockQuantity() < cartItemRequest.getQuantity()){
            return false; // Not enough stock
        }

        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));

        if(userOptional.isEmpty()) { return false; }

        User user=userOptional.get();

        //If the product is found in the cart so we'll increase the quantity
        CartItem existingCartItem=cartItemRepository.findByUserAndProduct(user,product);

        if(existingCartItem!=null){
            //Update the quantity and price
            existingCartItem.setQuantity(existingCartItem.getQuantity()+cartItemRequest.getQuantity());
            existingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
            cartItemRepository.save(existingCartItem);
        }else{
            //Create New Cart Item
            CartItem newCartItem=new CartItem();
            newCartItem.setUser(user);
            newCartItem.setProduct(product);
            newCartItem.setQuantity(cartItemRequest.getQuantity());
            newCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(cartItemRequest.getQuantity())));
            cartItemRepository.save(newCartItem);
        }

        return true;
    }


    public List<CartItemResponse> getCartItems(String userId) {
//        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));
//        if(userOptional.isEmpty()) { return List.of(); }
//
//        List<CartItem> cartItems=cartItemRepository.findByUser(userOptional.get());
//        return cartItems.stream().map(cartItem -> {
//            CartItemResponse response = new CartItemResponse();
//            response.setProduct(cartItem.getProduct());
//            response.setQuantity(cartItem.getQuantity());
//            response.setUser(cartItem.getUser());
//            response.setPrice(cartItem.getPrice());
//            return response;
//
//        }).toList();

        //Approach 2 neat way
        return userRepository.findById(Long.valueOf(userId))
                .map(user -> cartItemRepository.findByUser(user).stream()
                        .map(cartItem -> {
                            CartItemResponse response = new CartItemResponse();
                            response.setProduct(cartItem.getProduct());
                            response.setQuantity(cartItem.getQuantity());
                            response.setPrice(cartItem.getPrice());
                            return response;
                        })
                        .toList() // collect stream to list
                )
                .orElseGet(List::of);

    }

    public boolean removeFromCart(String userId, Long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));

        if(userOptional.isEmpty() || productOptional.isEmpty()) { return false; }
        //.get() returns the actual entity (User, Product)
        cartItemRepository.deleteByUserAndProduct(
                userOptional.get(),
                productOptional.get()
        );


        return true;


        //Approach 1 DIDNT WORK
        //We use flatMap on userOptional because its lambda returns an Optional from productOptional.map, and we use map on productOptional because its lambda returns a direct value (Boolean).
        //This expression creates an Optional<Booleann>.But you did not return it. You also did not store it. Java throws it away.

        //Why “return true” didn’t work ->Returning inside a lambda ≠ returning from the method. They are different scopes.

        //404 occurred because true was returned only from the lambda, not from the service method, so the controller received false.
//        userOptional.flatMap(user->
//                productOptional.map(product -> {
//                    cartItemRepository.deleteByUserAndProduct(user, product);
//                    return true;    // Returning true from the INNER lambda
//
//                })
//
//        );



        //Approach 2
//        if (userOptional.isPresent() && productOptional.isPresent()) {
//            cartItemRepository.deleteByUserAndProduct(
//                    userOptional.get(),
//                    productOptional.get()
//            );
//
//            return true;
//        }

        //Approach 3
        //ifPresent returns void
        //👉 Consumer means: “take a value, do something, return nothing”
        //We can't return true from inside the lambda because the lambda is of type Consumer which returns void.
//        userOptional.ifPresent(user ->
//                productOptional.ifPresent(product ->{
//                        cartItemRepository.deleteByUserAndProduct(user, product);
//
//                })
//
//        );


    }





}
