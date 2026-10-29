package com.example.QuickBite.order;

import com.example.QuickBite.address.Address;
import com.example.QuickBite.address.AddressRepository;
import com.example.QuickBite.cart.CartItem;
import com.example.QuickBite.cart.CartService;
import com.example.QuickBite.exception.OrderNotFoundException;
import com.example.QuickBite.menu.MenuItem;
import com.example.QuickBite.restaurant.Restaurant;
import com.example.QuickBite.user.User;
import com.example.QuickBite.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final UserService userService;
    private final CartService cartService;

    public OrderService(
            OrderRepository orderRepository,
            AddressRepository addressRepository,
            UserService userService,
            CartService cartService
    ) {
        this.orderRepository = orderRepository;
        this.addressRepository = addressRepository;
        this.userService = userService;
        this.cartService = cartService;
    }

    @Transactional
    public Order createOrder(Long userId, Long addressId) {

        User user = userService.getUserById(userId);

        Address deliveryAddress = addressRepository.findById(addressId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Address not found"));

        boolean addressBelongsToUser = user.getAddresses()
                .stream()
                .anyMatch(address ->
                        address.getId() != null
                                && address.getId().equals(addressId));

        if (!addressBelongsToUser) {
            throw new IllegalArgumentException(
                    "Delivery address does not belong to user"
            );
        }

        List<CartItem> cartItems =
                cartService.getAllItemsByCart(userId);

        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException(
                    "Cannot create an order with an empty cart"
            );
        }

        Restaurant restaurant = cartItems.get(0)
                .getMenuItem()
                .getRestaurant();

        if (!restaurant.isActive()) {
            throw new IllegalArgumentException(
                    "Restaurant is not active"
            );
        }

        Order order = new Order();

        order.setUser(user);
        order.setRestaurant(restaurant);
        order.setDeliveryAddress(deliveryAddress);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {

            MenuItem menuItem = cartItem.getMenuItem();

            if (!menuItem.getAvailable()) {
                throw new IllegalArgumentException(
                        "Menu item is not available: "
                                + menuItem.getName()
                );
            }

            if (!java.util.Objects.equals(
                    menuItem.getRestaurant().getId(),
                    restaurant.getId())) {
                throw new IllegalArgumentException(
                        "All items must belong to the same restaurant"
                );
            }

            int quantity = cartItem.getQuantity();

            if (quantity <= 0) {
                throw new IllegalArgumentException(
                        "Quantity must be greater than 0"
                );
            }

            BigDecimal unitPrice = menuItem.getPrice();

            BigDecimal lineTotal = unitPrice.multiply(
                    BigDecimal.valueOf(quantity)
            );

            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);
            orderItem.setMenuItem(menuItem);
            orderItem.setMenuItemName(menuItem.getName());
            orderItem.setUnitPrice(unitPrice);
            orderItem.setQuantity(quantity);
            orderItem.setLineTotal(lineTotal);

            order.getItems().add(orderItem);

            totalPrice = totalPrice.add(lineTotal);
        }

        order.setTotalPrice(totalPrice);

        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(userId);

        return savedOrder;
    }

    @Transactional(readOnly = true)
    public Order getOrderById(Long orderId) {

        return orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new OrderNotFoundException("Order not found"));
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersByUserId(Long userId) {

        userService.getUserById(userId);

        return orderRepository
                .findAllByUser_IdOrderByCreatedAtDesc(userId);
    }

    @Transactional
    public Order updateOrderStatus(
            Long orderId,
            OrderStatus newStatus
    ) {
        Order order = getOrderById(orderId);

        if (!isValidStatusTransition(order.getStatus(), newStatus)) {
            throw new IllegalArgumentException(
                    "Invalid order status transition: "
                            + order.getStatus()
                            + " -> "
                            + newStatus
            );
        }

        order.setStatus(newStatus);

        return orderRepository.save(order);
    }

    private boolean isValidStatusTransition(
            OrderStatus currentStatus,
            OrderStatus newStatus
    ) {
        if (currentStatus == null || newStatus == null) {
            return false;
        }

        switch (currentStatus) {
            case PENDING:
                return newStatus == OrderStatus.CONFIRMED
                        || newStatus == OrderStatus.CANCELLED;

            case CONFIRMED:
                return newStatus == OrderStatus.PREPARING
                        || newStatus == OrderStatus.CANCELLED;

            case PREPARING:
                return newStatus == OrderStatus.READY_FOR_PICKUP;

            case READY_FOR_PICKUP:
                return newStatus == OrderStatus.IN_DELIVERY;

            case IN_DELIVERY:
                return newStatus == OrderStatus.DELIVERED;

            case DELIVERED:
            case CANCELLED:
            default:
                return false;
        }
    }
}