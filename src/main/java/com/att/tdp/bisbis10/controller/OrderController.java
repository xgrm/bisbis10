package com.att.tdp.bisbis10.controller;

import com.att.tdp.bisbis10.dto.OrderDTO;
import com.att.tdp.bisbis10.model.Restaurant;
import com.att.tdp.bisbis10.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody OrderDTO content) {
        if (!orderService.existsRestaurantAndDishesId(content)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dish not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body("{\"orderId\": "+orderService.save(content)+ "\"}");
    }
}
