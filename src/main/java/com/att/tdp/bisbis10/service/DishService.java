package com.att.tdp.bisbis10.service;

import com.att.tdp.bisbis10.dto.DishDTO;
import com.att.tdp.bisbis10.model.Dish;
import com.att.tdp.bisbis10.repository.DishRepository;
import com.att.tdp.bisbis10.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishService {
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;

    public DishService(DishRepository dishRepository, RestaurantRepository restaurantRepository) {
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public List<DishDTO> findByDishesByRestaurantId(Integer id) {
        return dishRepository.findByRestaurantIdAndIsDeleted(id,false).stream()
                .map(dish -> new DishDTO(dish)).collect(Collectors.toList());
    }
    @Modifying
    @Transactional
    public void deleteByRestaurantId(Integer id) {
        dishRepository.deleteByRestaurantId(id);
    }
    public Boolean existsByIdAndRestaurantId(Integer id, Integer restaurantId){
        return dishRepository.existsByIdAndRestaurantId(id,restaurantId);
    }

    public boolean existsRestaurantById(Integer restaurantId) {
        return restaurantRepository.existsById(restaurantId);
    }

    public void save(Dish content) {
        dishRepository.save(content);
    }
}
