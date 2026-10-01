package com.example.QuickBite.address;

import com.example.QuickBite.restaurant.Restaurant;
import com.example.QuickBite.restaurant.RestaurantService;
import com.example.QuickBite.user.User;
import com.example.QuickBite.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserService userService;
    private final RestaurantService restaurantService;


    public AddressService(AddressRepository addressRepository, UserService userService, RestaurantService restaurantService) {
        this.addressRepository = addressRepository;
        this.userService = userService;
        this.restaurantService = restaurantService;
    }

    public List<Address> getAllAddresses(){
        return addressRepository.findAll();
    }

    @Transactional
    public Address createAddressForUser(Long userId, Address address) {

        User user = userService.getUserById(userId);

        Address savedAddress = addressRepository.save(address);

        user.getAddresses().add(savedAddress);

        userService.saveUser(user);

        return savedAddress;
    }

    public List<Address> getAddressByUserId (Long userId){
        User user = userService.getUserById(userId);
        return user.getAddresses();
    }

    @Transactional
    public Address createAddressForRestaurant (Long restaurantId, Address address){

        restaurantService.getRestaurantById(restaurantId);
        Address savedAddress = addressRepository.save(address);

        restaurantService.addAddressToRestaurant(restaurantId,savedAddress);

        return savedAddress;
    }

    public Address getAddressByRestaurantId(Long restaurantId){
        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
        return restaurant.getRestaurantAddress();
    }



}
