package com.example.QuickBite.menu;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;

@RestController
@RequestMapping("/menu-items")

public class MenuItemController {

    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @GetMapping
    public List<MenuItem> getAllMenuItems(){

        return menuItemService.getAllMenuItems();

    }

    @PostMapping("restaurant/{restaurantId}")
    public MenuItem createMenuItem(@PathVariable Long restaurantId,@Valid @RequestBody MenuItem menuItem){
        return menuItemService.createMenuItem(restaurantId,menuItem);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public List<MenuItem> getMenuItemsByRestaurantId(@PathVariable Long restaurantId){
        return menuItemService.getMenuItemsByRestaurantId(restaurantId);
    }

    @GetMapping("/{id}")
    public MenuItem getMenuItem(@PathVariable Long id){
        return menuItemService.getMenuItemById(id);
    }

    @PutMapping("/{id}")
    public MenuItem updateMenuItem(@PathVariable Long id,@Valid @RequestBody MenuItem menuItem){
        return menuItemService.updateMenuItem(id,menuItem);
    }

    @DeleteMapping("/{id}")
    public void deleteMenuItem(@PathVariable Long id){
        menuItemService.deleteMenuItemById(id);
    }

}
