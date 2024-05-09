package com.att.tdp.bisbis10.service;

import com.att.tdp.bisbis10.dto.RestaurantDTO;
import com.att.tdp.bisbis10.dto.RestaurantWithDishesDTO;
import com.att.tdp.bisbis10.model.Dish;
import com.att.tdp.bisbis10.model.Restaurant;
import com.att.tdp.bisbis10.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    public Optional<Restaurant> findById(Integer id) {
        return restaurantRepository.findById(id);
    }

    public void update(Map<String, Object> updates, Restaurant existingRestaurant) throws IllegalArgumentException{
        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    if (value instanceof String) {
                        existingRestaurant.setName((String) value);
                    } else {
                        throw new IllegalArgumentException("Value for 'name' must be a String");
                    }
                    break;
                case "isKosher":
                    if (value instanceof Boolean) {
                        existingRestaurant.setKosher((Boolean) value);
                    } else {
                        throw new IllegalArgumentException("Value for 'isKosher' must be a Boolean");
                    }
                    break;
                case "cuisines":
                    if (value instanceof List) {
                        @SuppressWarnings("unchecked")
                        List<String> cuisines = (List<String>) value;
                        existingRestaurant.setCuisines(cuisines);
                    } else {
                        throw new IllegalArgumentException("Value for 'cuisines' must be a List<String>");
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Invalid key: " + key);
            }
        });

        this.save(existingRestaurant);
    }
}
