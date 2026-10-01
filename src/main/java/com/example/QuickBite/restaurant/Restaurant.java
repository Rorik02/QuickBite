package com.example.QuickBite.restaurant;


import com.example.QuickBite.address.Address;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Restaurant {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private long id;

@NotBlank
private String name;
private String description;
@NotBlank
private String address;
private String phoneNumber;
private boolean active;

@OneToOne
@JoinColumn(name = "address_id")
private Address restaurantAddress;

    public Restaurant(long id, String name, String description, String address, String phoneNumber, boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.active = active;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isActive() {
        return active;
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Restaurant() {
    }

    public Address getRestaurantAddress() {
        return restaurantAddress;
    }

    public void setRestaurantAddress(Address restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }
}
