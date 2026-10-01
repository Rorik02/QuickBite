package com.example.QuickBite.address;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addresses")
public class AddressController {
    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public List<Address> getAllAddresses(){
        return addressService.getAllAddresses();
    }

    @PostMapping("/user/{userId}")
    public Address createAddressForUser(@PathVariable Long userId,@Valid @RequestBody Address address){
        return addressService.createAddressForUser(userId,address);
    }

    @GetMapping("/user/{userId}")
    public List<Address> getAddressByUserId(@PathVariable Long userId){
        return addressService.getAddressByUserId(userId);
    }

    @PostMapping("/restaurant/{restaurantId}")
    public Address createAddressForRestaurant(@PathVariable Long restaurantId,@Valid @RequestBody Address address){
        return addressService.createAddressForRestaurant(restaurantId,address);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public Address getAddressByRestaurantId(@PathVariable Long restaurantId){
        return addressService.getAddressByRestaurantId(restaurantId);
    }

}
