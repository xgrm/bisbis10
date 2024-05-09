package com.att.tdp.bisbis10.service;

import com.att.tdp.bisbis10.dto.DishDTO;
import com.att.tdp.bisbis10.model.Dish;
import com.att.tdp.bisbis10.repository.DishRepository;
import com.att.tdp.bisbis10.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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
        return dishRepository.existsByIdAndRestaurantIdAndIsDeleted(id,restaurantId,false);
    }
    public boolean existsRestaurantById(Integer restaurantId) {
        return restaurantRepository.existsById(restaurantId);
    }
    public void save(Dish content) {
        dishRepository.save(content);
    }
    @Modifying
    public void update(Map<String, Object> updates, Integer dishId, Integer restaurantId) throws IllegalArgumentException{
        Dish existingDish = dishRepository.getDishByIdAndRestaurantId(dishId,restaurantId);
        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    if (value instanceof String) {existingDish.setName((String) value);}
                    else throw new IllegalArgumentException("Value for 'name' must be a String");
                    break;
                case "description":
                    if (value instanceof String){existingDish.setDescription((String) value);}
                    else throw new IllegalArgumentException("Value for 'description' must be a String");
                    break;
                case "price":
                    if (value instanceof Double) {existingDish.setPrice((Double) value);}
                    else if (value instanceof Integer) {
                        Integer v = (Integer)value;
                        existingDish.setPrice(v.doubleValue());}
                    else throw new IllegalArgumentException("Value for 'price' must be a Number");
                    break;
                default:
                    throw new IllegalArgumentException("Invalid key: " + key);
            }
        });

        this.save(existingDish);
    }
}
