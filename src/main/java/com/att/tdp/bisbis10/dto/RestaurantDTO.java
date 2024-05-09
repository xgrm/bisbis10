package com.att.tdp.bisbis10.dto;


import com.att.tdp.bisbis10.model.Restaurant;
import lombok.Data;


import java.util.List;
@Data
public class RestaurantDTO {
    private Integer id;
    private String name;
    private Double averageRating;
    private boolean isKosher;
    private List<String> cuisines;

    public RestaurantDTO(Restaurant restaurant) {
        this.setId(restaurant.getId());
        this.setName(restaurant.getName());
        this.setAverageRating(restaurant.getAverageRating());

        this.setIsKosher(restaurant.isKosher());
        this.setCuisines(restaurant.getCuisines());
    }

    public boolean getIsKosher() {
        return isKosher;
    }

    public void setIsKosher(boolean kosher) {
        isKosher = kosher;
    }
}
