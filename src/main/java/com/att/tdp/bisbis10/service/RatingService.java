package com.att.tdp.bisbis10.service;

import com.att.tdp.bisbis10.model.Rating;
import com.att.tdp.bisbis10.repository.RatingRepository;
import com.att.tdp.bisbis10.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;
    private final RestaurantRepository restaurantRepository;
    public RatingService(RatingRepository ratingRepository, RestaurantRepository restaurantRepository) {
        this.ratingRepository = ratingRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public Double findAverageRatingByRestaurantId(Integer restaurantId) {
        return ratingRepository.findAverageRatingByRestaurantId(restaurantId);
    }
    @Modifying
    @Transactional
    public void deleteByRestaurantId(Integer restaurantId) {
        ratingRepository.deleteByRestaurantId(restaurantId);
    }
    public boolean existsRestaurantById(Integer restaurantId){
        return restaurantRepository.existsById(restaurantId);
    }
    public void save(Rating content) {
        ratingRepository.save(content);
    }
}
