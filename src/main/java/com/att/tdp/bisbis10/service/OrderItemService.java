package com.att.tdp.bisbis10.service;

import com.att.tdp.bisbis10.repository.OrderItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

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
}
