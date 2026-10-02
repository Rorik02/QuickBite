package com.example.QuickBite.cart;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/user/{userId}")
    public Cart createCart(@PathVariable Long userId){
        return cartService.createCart(userId);
    }

    @GetMapping("/user/{userId}")
    public Cart findCartByUserId(@PathVariable Long userId){
        return cartService.findCartByUserId(userId);
    }

    @GetMapping("/user/{userId}/items")
    public List<CartItem> getAllItemsByCart(@PathVariable Long userId){
        return cartService.getAllItemsByCart(userId);
    }

    @PostMapping("/user/{userId}/items/{menuItemId}")
    public CartItem addItemToCart(@PathVariable Long userId, @PathVariable Long menuItemId,@RequestParam int quantity){
        return cartService.addItemToCart(userId,menuItemId,quantity);
    }

    @DeleteMapping("/items/{cartItemId}")
    public void deleteItemFromCart(@PathVariable Long cartItemId){
        cartService.deleteItemFromCart(cartItemId);
    }

    @GetMapping("/user/{userId}/total")
    public BigDecimal calculateCartTotal(@PathVariable Long userId) {
        return cartService.calculateCartTotal(userId);
    }

    @PutMapping("/items/{cartItemId}")
    public CartItem updateItemQuantity(@PathVariable Long cartItemId, @RequestParam int quantity) {
        return cartService.updateItemQuantity(cartItemId, quantity);
    }

    @DeleteMapping("/user/{userId}/items")
    public void clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
    }
}
