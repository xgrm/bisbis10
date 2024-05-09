package com.att.tdp.bisbis10.service;

import com.att.tdp.bisbis10.dto.RestaurantDTO;
import com.att.tdp.bisbis10.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final RatingService ratingService;
    public RestaurantService(RestaurantRepository restaurantRepository, RatingService ratingService) {
        this.restaurantRepository = restaurantRepository;
        this.ratingService = ratingService;
    }

    public List<RestaurantDTO> findAll() {
        return restaurantRepository.findAll().stream().map(restaurant -> {
            restaurant.setAverageRating(ratingService.findAverageRatingByRestaurantId(restaurant.getId()));
            return new RestaurantDTO(restaurant);
        }).collect(Collectors.toList());
    }
}
