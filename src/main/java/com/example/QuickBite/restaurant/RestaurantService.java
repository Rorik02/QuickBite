package com.example.QuickBite.restaurant;


import com.example.QuickBite.exception.RestaurantNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public List<Restaurant> getAllRestaurants(){
        return restaurantRepository.findAll();
    }

    public Restaurant createRestaurant(Restaurant restaurant){
        return restaurantRepository.save(restaurant);
    }

    public Restaurant getRestaurantById(Long id){

        return restaurantRepository
                .findById(id)
                .orElseThrow (()-> new RestaurantNotFoundException("Restaurant not found"));
    }

    public void deleteRestaurantById(Long id){

        getRestaurantById(id);
        restaurantRepository.deleteById(id);

    }

    public Restaurant updateRestaurant(Long id, Restaurant restaurant){

        Restaurant existingRestaurant = getRestaurantById(id);

        String name = restaurant.getName();
        existingRestaurant.setName(name);

        String description = restaurant.getDescription();
        existingRestaurant.setDescription(description);

        String address = restaurant.getAddress();
        existingRestaurant.setAddress(address);

        String phoneNumber = restaurant.getPhoneNumber();
        existingRestaurant.setPhoneNumber(phoneNumber);

        boolean active = restaurant.isActive();
        existingRestaurant.setActive(active);

        return restaurantRepository.save(existingRestaurant);
    }

}
