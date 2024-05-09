package com.att.tdp.bisbis10.repository;

import com.att.tdp.bisbis10.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.restaurantId = :restaurantId")
    Double findAverageRatingByRestaurantId(@Param("restaurantId") Integer restaurantId);
}
