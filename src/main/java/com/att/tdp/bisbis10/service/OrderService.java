package com.att.tdp.bisbis10.service;

import com.att.tdp.bisbis10.model.Order;
import com.att.tdp.bisbis10.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;
    public OrderService(OrderRepository orderRepository, OrderItemService orderItemService) {
        this.orderRepository = orderRepository;
        this.orderItemService = orderItemService;
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
}
