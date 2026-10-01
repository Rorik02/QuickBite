package com.example.QuickBite.address;

import com.example.QuickBite.user.User;
import com.example.QuickBite.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserService userService;


    public AddressService(AddressRepository addressRepository, UserService userService) {
        this.addressRepository = addressRepository;
        this.userService = userService;
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

}
