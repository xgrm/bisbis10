package com.att.tdp.bisbis10.service;

import com.att.tdp.bisbis10.dto.RestaurantDTO;
import com.att.tdp.bisbis10.dto.RestaurantWithDishesDTO;
import com.att.tdp.bisbis10.model.Dish;
import com.att.tdp.bisbis10.model.Restaurant;
import com.att.tdp.bisbis10.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final RatingService ratingService;
    private final DishService dishService;
    public RestaurantService(RestaurantRepository restaurantRepository, RatingService ratingService, DishService dishService) {
        this.restaurantRepository = restaurantRepository;
        this.ratingService = ratingService;
        this.dishService = dishService;
    }

    public List<RestaurantDTO> findAll() {
        return restaurantRepository.findAll().stream().map(restaurant ->covertToDTO(restaurant)).collect(Collectors.toList());
    }

    public List<RestaurantDTO> GetRestaurantsByCuisine(String cuisine) {
        return restaurantRepository.findByCuisine(cuisine).stream().map(restaurant ->covertToDTO(restaurant)).collect(Collectors.toList());
    }
    private RestaurantDTO covertToDTO(Restaurant restaurant){
        restaurant.setAverageRating(ratingService.findAverageRatingByRestaurantId(restaurant.getId()));
        return new RestaurantDTO(restaurant);
    }
    public boolean existsById(Integer id) {
        return restaurantRepository.existsById(id);
    }

    public RestaurantWithDishesDTO getRestaurantById(Integer id) {
        return new RestaurantWithDishesDTO(covertToDTO(restaurantRepository.findById(id).get()),dishService.findByDishesByRestaurantId(id));
    }

    public void save(Restaurant content) {
        restaurantRepository.save(content);
    }
}
