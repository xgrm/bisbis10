package com.att.tdp.bisbis10.controller;

import com.att.tdp.bisbis10.dto.DishDTO;
import com.att.tdp.bisbis10.model.Dish;
import com.att.tdp.bisbis10.service.DishService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

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
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{restaurant_id}/dishes/{dishId}")
    public void update(@RequestBody Map<String, Object> updates, @PathVariable Integer restaurant_id, @PathVariable Integer dishId) {
        if(!dishService.existsByIdAndRestaurantId(dishId,restaurant_id) ) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dish not found.");
        }
        try {
            dishService.update(updates, dishId,restaurant_id);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{restaurant_id}/dishes/{dishId}")
    public void delete(@PathVariable Integer restaurant_id,@PathVariable Integer dishId){
        if(!dishService.existsByIdAndRestaurantId(dishId,restaurant_id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dish not found.");
        }
        dishService.delete(restaurant_id,dishId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{restaurant_id}/dishes")
    public List<DishDTO> getDishesByRestaurant(@PathVariable Integer restaurant_id){
        if(!dishService.existsRestaurantById(restaurant_id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found.");
        }
        return dishService.findByDishesByRestaurantId(restaurant_id);
    }
}
