package com.att.tdp.bisbis10.controller;

import com.att.tdp.bisbis10.dto.RestaurantDTO;
import com.att.tdp.bisbis10.service.RestaurantService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
