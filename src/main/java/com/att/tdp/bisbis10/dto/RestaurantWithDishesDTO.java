package com.att.tdp.bisbis10.dto;


import com.att.tdp.bisbis10.model.Restaurant;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RestaurantWithDishesDTO extends RestaurantDTO {
    private List<DishDTO> dishes;

    public RestaurantWithDishesDTO(RestaurantDTO restaurant, List<DishDTO> dishDTOS) {
        super();
        this.setId(restaurant.getId());
        this.setName(restaurant.getName());
        this.setAverageRating(restaurant.getAverageRating());
        this.setIsKosher(restaurant.getIsKosher());
        this.setCuisines(restaurant.getCuisines());
        this.setDishes(dishDTOS);
    }
}
