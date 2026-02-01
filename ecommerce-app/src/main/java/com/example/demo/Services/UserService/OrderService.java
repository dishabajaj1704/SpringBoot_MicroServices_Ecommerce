package com.example.demo.Services.UserService;



import com.example.demo.DTOS.OrderItemDTO;
import com.example.demo.DTOS.OrderResponse;
import com.example.demo.Entities.CartItem;
import com.example.demo.Entities.Order;
import com.example.demo.Entities.OrderItem;
import com.example.demo.Entities.User;
import com.example.demo.Enums.OrderStatus;
import com.example.demo.Repositories.OrderRepository;
import com.example.demo.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final UserRepository userRepository;

    public Optional<OrderResponse> createOrder(String userId) {

        //validation for cart Items
        List<CartItem> cartItems =cartService.getCartItems(userId);
        if(cartItems.isEmpty()){return Optional.empty();}

        //Validation for User
        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));
        if(userOptional.isEmpty()){return Optional.empty();}
        User user=userOptional.get();

        //collect total Price
        //reduce syntax is (initial value, binary operator)
        BigDecimal totalPrice = cartItems.stream()
                .map(CartItem::getPrice) //Picking price from each cart item
                .reduce(BigDecimal.ZERO, BigDecimal::add); //Summing up all prices



        //Create an Order

        Order order=new Order();
        order.setUser(user);
        order.setTotalAmount(totalPrice);
        order.setStatus(OrderStatus.CONFIRMED);
        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> new OrderItem(
                            null,
                            cartItem.getQuantity(),
                            cartItem.getProduct(),
                            cartItem.getPrice(),
                            order
                        )
                ).toList();
        order.setOrderItems(orderItems);
        Order savedOrder= orderRepository.save(order);


        //Clear the cart
        cartService.clearCart(userId);


        return Optional.of(mapToOrderResponse(savedOrder));

    }


    private OrderResponse mapToOrderResponse(Order order){
        OrderResponse orderResponse=new OrderResponse();
        orderResponse.setId(order.getId());
        orderResponse.setTotalAmount(order.getTotalAmount());
        orderResponse.setStatus(order.getStatus());
        orderResponse.setOrderItems(order.getOrderItems().stream().map(
                orderItem -> new OrderItemDTO(
                        orderItem.getId(),
                        orderItem.getProduct().getId(),
                        orderItem.getQuantity(),
                        orderItem.getPrice(),
                        orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())) //subTotal

        )).toList());

        orderResponse.setCreatedAt(order.getCreatedAt());
        return orderResponse;
    }
}
