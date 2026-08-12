package com.sales.backend.SalesBackend.repositories;

import com.sales.backend.SalesBackend.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Integer> {
}
