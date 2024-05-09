package com.att.tdp.bisbis10.controller;

import com.att.tdp.bisbis10.model.Dish;
import com.att.tdp.bisbis10.service.DishService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/restaurants")
public class DishController {
    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{restaurant_id}/dishes")
    public void addADish(@PathVariable Integer restaurant_id, @Valid @RequestBody Dish content) {
        if(!dishService.existsRestaurantById(restaurant_id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found.");
        }
        content.setRestaurantId(restaurant_id);
        dishService.save(content);
    }
}
