package com.att.tdp.bisbis10.service;

import com.att.tdp.bisbis10.dto.DishDTO;
import com.att.tdp.bisbis10.repository.DishRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishService {
    private final DishRepository dishRepository;

    public DishService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
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
}
