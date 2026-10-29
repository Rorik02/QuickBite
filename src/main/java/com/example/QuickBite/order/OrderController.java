package com.example.QuickBite.order;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/user/{userId}")
    public Order createOrder(
            @PathVariable Long userId,
            @RequestParam Long addressId
    ) {
        return orderService.createOrder(userId, addressId);
    }

    @GetMapping("/{orderId}")
    public Order getOrderById(
            @PathVariable Long orderId
    ) {
        return orderService.getOrderById(orderId);
    }

    @GetMapping("/user/{userId}")
    public List<Order> getOrdersByUserId(
            @PathVariable Long userId
    ) {
        return orderService.getOrdersByUserId(userId);
    }

    @PutMapping("/{orderId}/status")
    public Order updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status
    ) {
        return orderService.updateOrderStatus(orderId, status);
    }
}