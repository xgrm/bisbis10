package com.att.tdp.bisbis10.service;

import com.att.tdp.bisbis10.dto.OrderDTO;
import com.att.tdp.bisbis10.model.Order;
import com.att.tdp.bisbis10.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;
    private final DishService dishService;
    public OrderService(OrderRepository orderRepository, OrderItemService orderItemService, DishService dishService) {
        this.orderRepository = orderRepository;
        this.orderItemService = orderItemService;
        this.dishService = dishService;
    }
    public List<Order> getOrdersByRestaurantId(Integer id){
        return orderRepository.getAllByRestaurant(id);
    }
    @Modifying
    @Transactional
    public void deleteByRestaurantId(Integer id) {
        getOrdersByRestaurantId(id).stream().forEach(order -> {
            orderItemService.deleteByOrderUUID(order.getId());
            orderRepository.delete(order);
        });
    }
    public UUID save(OrderDTO content) {
        Order order = orderRepository.save(new Order().builder().restaurant(content.getRestaurantId()).build());
        orderItemService.saveOrderItems(content.getOrderItems(),order.getId());
        return  order.getId();
    }
    public boolean existsRestaurantAndDishesId(OrderDTO content) {
        return content.getOrderItems().stream().
                        allMatch(orderItemDTO -> dishService.existsByIdAndRestaurantId(orderItemDTO.getDishId(),content.getRestaurantId()));
    }
}
