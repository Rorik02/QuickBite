package com.example.QuickBite.restaurant;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public List<Restaurant> getAllRestaurants(){
        return restaurantService.getAllRestaurants();
    }

    @PostMapping
    public Restaurant createRestaurant(@Valid @RequestBody Restaurant restaurant){
        return restaurantService.createRestaurant(restaurant);
    }

    @GetMapping("/{id}")
    public Restaurant getRestaurantById (@PathVariable Long id){
        return restaurantService.getRestaurantById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteRestaurantById(@PathVariable Long id) {
        restaurantService.deleteRestaurantById(id);
    }

    @PutMapping("/{id}")
    public Restaurant updateRestaurant(@PathVariable long id,@Valid @RequestBody Restaurant restaurant){
        return restaurantService.updateRestaurant(id,restaurant);
    }

}
