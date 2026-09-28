package com.example.QuickBite.menu;

import com.example.QuickBite.exception.MenuItemNotFoundException;
import com.example.QuickBite.restaurant.Restaurant;
import com.example.QuickBite.restaurant.RestaurantService;
import org.springframework.stereotype.Service;

import javax.naming.Name;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantService restaurantService;

    public MenuItemService(MenuItemRepository menuItemRepository, RestaurantService restaurantService) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantService = restaurantService;
    }

    public List<MenuItem> getAllMenuItems(){

        return menuItemRepository.findAll();

    }


    public MenuItem createMenuItem(Long restaurantId, MenuItem menuItem) {

        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);

        menuItem.setRestaurant(restaurant);

        return menuItemRepository.save(menuItem);
    }

    public List<MenuItem> getMenuItemsByRestaurantId(Long restaurantId){

        return menuItemRepository.findAllByRestaurant_Id(restaurantId);

    }

    public MenuItem getMenuItemById(Long id){
        return menuItemRepository
                .findById(id)
                .orElseThrow(()-> new MenuItemNotFoundException("Menu item not found"));
    }


    public MenuItem updateMenuItem(Long id,MenuItem menuItem){
        MenuItem existingMenuItem;
        existingMenuItem = getMenuItemById(id);

        String name = menuItem.getName();
        existingMenuItem.setName(name);

        String description = menuItem.getDescription();
        existingMenuItem.setDescription(description);

        BigDecimal price = menuItem.getPrice();
        existingMenuItem.setPrice(price);

        boolean available = menuItem.getAvailable();
        existingMenuItem.setAvailable(available);

        return menuItemRepository.save(existingMenuItem);

    }

    public void deleteMenuItemById(Long id){
        getMenuItemById(id);
        menuItemRepository.deleteById(id);
    }
}
