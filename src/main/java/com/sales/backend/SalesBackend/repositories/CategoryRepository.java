package com.sales.backend.SalesBackend.repositories;

import com.sales.backend.SalesBackend.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {

}
