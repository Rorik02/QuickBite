package com.example.QuickBite.cart;

import com.example.QuickBite.exception.CartItemNotFoundException;
import com.example.QuickBite.exception.CartNotFoundException;
import com.example.QuickBite.menu.MenuItem;
import com.example.QuickBite.menu.MenuItemRepository;
import com.example.QuickBite.menu.MenuItemService;
import com.example.QuickBite.user.User;
import com.example.QuickBite.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserService userService;
    private final CartItemRepository cartItemRepository;
    private final MenuItemService menuItemService;

    public CartService(CartRepository cartRepository, UserService userService, CartItemRepository cartItemRepository, MenuItemService menuItemService) {
        this.cartRepository = cartRepository;
        this.userService = userService;
        this.cartItemRepository = cartItemRepository;
        this.menuItemService = menuItemService;
    }

    public Cart findCartByUserId(Long userId){
        return cartRepository
                .findByUser_Id(userId)
                .orElseThrow(()->new CartNotFoundException("Cart not found"));
    }

    public Cart createCart(Long userId){
        User user = userService.getUserById(userId);
        Optional<Cart> existingCart = cartRepository.findByUser_Id(userId);
        if (existingCart.isPresent()) {

            return existingCart.get();
        }

        Cart cart = new Cart();
        cart.setUser(user);

        return cartRepository.save(cart);
    }

    public CartItem addItemToCart(Long userId, Long menuItemId, int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        Cart cart = findCartByUserId(userId);
        MenuItem menuItem = menuItemService.getMenuItemById(menuItemId);

        if (!menuItem.isAvailable()) {
            throw new IllegalArgumentException("Menu item is not available");
        }

        if (!menuItem.getRestaurant().isActive()) {
            throw new IllegalArgumentException("Restaurant is not active");
        }

        List<CartItem> items = cartItemRepository.findAllByCart_Id(cart.getId());

        if (!items.isEmpty()) {
            Long restaurantIdInCart = items.get(0)
                    .getMenuItem()
                    .getRestaurant()
                    .getId();

            Long newRestaurantId = menuItem
                    .getRestaurant()
                    .getId();

            if (!restaurantIdInCart.equals(newRestaurantId)) {
                throw new IllegalArgumentException(
                        "You can only add items from one restaurant to the cart"
                );
            }
        }

        Optional<CartItem> cartItemOptional =
                cartItemRepository.findByCart_IdAndMenuItem_Id(cart.getId(), menuItemId);

        if (cartItemOptional.isPresent()) {
            CartItem cartItem = cartItemOptional.get();

            int newQuantity = cartItem.getQuantity() + quantity;
            cartItem.setQuantity(newQuantity);

            return cartItemRepository.save(cartItem);
        }

        CartItem cartItem = new CartItem();

        cartItem.setCart(cart);
        cartItem.setMenuItem(menuItem);
        cartItem.setQuantity(quantity);

        return cartItemRepository.save(cartItem);
    }

    public List<CartItem> getAllItemsByCart(Long userId){
        Cart cart = findCartByUserId(userId);
        return cartItemRepository.findAllByCart_Id(cart.getId());

    }

    public void deleteItemFromCart(Long cartItemId){
        CartItem cartItem = cartItemRepository
                .findById(cartItemId)
                .orElseThrow(()->new CartItemNotFoundException("Cart not found"));

        cartItemRepository.delete(cartItem);

    }

    public BigDecimal calculateCartTotal(Long userId) {
        List<CartItem> items = getAllItemsByCart(userId);

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : items) {
            BigDecimal itemTotal = cartItem.getMenuItem()
                    .getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            total = total.add(itemTotal);
        }

        return total;
    }

    public CartItem updateItemQuantity(Long cartItemId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        CartItem cartItem = cartItemRepository
                .findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException("Cart Item not found"));

        cartItem.setQuantity(quantity);

        return cartItemRepository.save(cartItem);
    }

    @Transactional
    public void clearCart(Long userId) {
        Cart cart = findCartByUserId(userId);
        cartItemRepository.deleteAllByCart_Id(cart.getId());
    }


}
