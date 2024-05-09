package com.att.tdp.bisbis10.repository;

import com.att.tdp.bisbis10.model.OrderItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    @Modifying
    @Transactional
    void deleteAllByOrderId(UUID order_id);
}
