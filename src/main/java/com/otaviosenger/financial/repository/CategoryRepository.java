package com.otaviosenger.financial.repository;


import com.otaviosenger.financial.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
