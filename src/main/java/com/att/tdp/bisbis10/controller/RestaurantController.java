package com.att.tdp.bisbis10.controller;

import com.att.tdp.bisbis10.dto.RestaurantDTO;
import com.att.tdp.bisbis10.dto.RestaurantWithDishesDTO;
import com.att.tdp.bisbis10.model.Restaurant;
import com.att.tdp.bisbis10.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<RestaurantDTO> getAllRestaurants() {
        return restaurantService.findAll();
    }
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(params = "cuisine")
    public List<RestaurantDTO> getRestaurantsByCuisine(@RequestParam("cuisine") String cuisine) {
        return restaurantService.GetRestaurantsByCuisine(cuisine);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public RestaurantWithDishesDTO getRestaurant(@PathVariable Integer id) {
        if (!restaurantService.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found.");
        }
        return restaurantService.getRestaurantById(id);
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void addARestaurant(@Valid @RequestBody Restaurant content) {
        restaurantService.save(content);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public void updateARestaurant(@RequestBody Map<String, Object> updates, @PathVariable Integer id) {
        Restaurant existingRestaurant = restaurantService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found."));
        try {
            restaurantService.update(updates, existingRestaurant);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
