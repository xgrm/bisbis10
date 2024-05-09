package com.att.tdp.bisbis10.repository;

import com.att.tdp.bisbis10.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish, Integer> {
}
