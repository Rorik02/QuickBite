package com.example.QuickBite.menu;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem,Long> {


    List<MenuItem> findAllByRestaurant_Id(Long restaurantId);

}
