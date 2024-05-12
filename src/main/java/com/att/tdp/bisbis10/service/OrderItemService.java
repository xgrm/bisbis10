package com.att.tdp.bisbis10.service;

import com.att.tdp.bisbis10.dto.OrderItemDTO;
import com.att.tdp.bisbis10.model.OrderItem;
import com.att.tdp.bisbis10.repository.OrderItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }
    @Modifying
    @Transactional
    public void deleteByOrderUUID(UUID orderUUID) {
        orderItemRepository.deleteAllByOrderId(orderUUID);
    }

    public void saveOrderItems(List<OrderItemDTO> orderItems,UUID order_id) {
        orderItems.stream().forEach(orderItemDTO -> orderItemRepository
                .save(new OrderItem().builder()
                        .dish_id(orderItemDTO.getDishId())
                        .amount(orderItemDTO.getAmount())
                        .orderId(order_id)
                        .build()));
    }
}
