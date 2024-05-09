package com.att.tdp.bisbis10.dto;

import com.att.tdp.bisbis10.model.Dish;
import lombok.Data;

@Data
public class DishDTO {
    private Integer id;
    private String name;
    private String description;
    private double price;

    public DishDTO(Dish dish) {
        this.setId(dish.getId());
        this.setName(dish.getName());
        this.setDescription(dish.getDescription());
        this.setPrice(dish.getPrice());
    }
}
