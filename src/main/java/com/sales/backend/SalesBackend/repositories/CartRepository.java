package com.sales.backend.SalesBackend.repositories;

import com.sales.backend.SalesBackend.entities.Cart;
import com.sales.backend.SalesBackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, String> {
    Optional<Cart> findByUser(User user);
}
