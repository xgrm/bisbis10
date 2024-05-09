package com.att.tdp.bisbis10.controller;

import com.att.tdp.bisbis10.model.Rating;
import com.att.tdp.bisbis10.service.RatingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/ratings")
public class RatingController {
    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public void create(@Valid @RequestBody Rating content) {
        if (!ratingService.existsRestaurantById(content.getRestaurantId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found.");
        }
        ratingService.save(content);
    }
}
