package com.sales.backend.SalesBackend.repositories;

import com.sales.backend.SalesBackend.entities.Order;
import com.sales.backend.SalesBackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findByUser(User user);

}
