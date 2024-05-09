package com.att.tdp.bisbis10.repository;

import com.att.tdp.bisbis10.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
}
