package com.sales.backend.SalesBackend.repositories;

import com.sales.backend.SalesBackend.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer>
{
}
