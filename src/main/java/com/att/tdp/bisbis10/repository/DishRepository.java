package com.att.tdp.bisbis10.repository;

import com.att.tdp.bisbis10.model.Dish;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Integer> {
    public List<Dish> findByRestaurantIdAndIsDeleted(Integer restaurantId, Boolean deleted);
    @Modifying
    @Transactional
    void deleteByRestaurantId(Integer restaurantId);

    Boolean existsByIdAndRestaurantIdAndIsDeleted(Integer id,Integer restaurantId, Boolean deleted);
    Dish getDishByIdAndRestaurantId(Integer id,Integer restaurantId);
}
