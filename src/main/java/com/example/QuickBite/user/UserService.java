package com.example.QuickBite.user;

import com.example.QuickBite.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public List<User> getAllUsers(){

        return userRepository.findAll();
    }


    public User createUser(User user){

        return userRepository.save(user);
    }

    public User getUserById(Long id){

        return userRepository
                .findById(id)
                .orElseThrow(()->new UserNotFoundException("User not found"));

    }

    public User updateUser(Long id,User user){
        User existingUser = getUserById(id);

        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getEmail();
        String password = user.getPassword();
        UserRole role = user.getRole();

        existingUser.setFirstName(firstName);
        existingUser.setLastName(lastName);
        existingUser.setEmail(email);
        existingUser.setPassword(password);
        existingUser.setRole(role);

        return userRepository.save(existingUser);

    }
}
