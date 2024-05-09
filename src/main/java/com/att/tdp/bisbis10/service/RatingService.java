package com.att.tdp.bisbis10.service;

import com.att.tdp.bisbis10.repository.RatingRepository;
import org.springframework.stereotype.Service;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public Double findAverageRatingByRestaurantId(Integer restaurantId) {
        return ratingRepository.findAverageRatingByRestaurantId(restaurantId);
    }
}
