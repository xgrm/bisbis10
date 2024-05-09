package com.att.tdp.bisbis10.repository;

import com.att.tdp.bisbis10.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Integer> {
    public List<Dish> findByRestaurantIdAndIsDeleted(Integer restaurantId, Boolean deleted);
}
