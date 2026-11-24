package com.otaviosenger.financial.repository;


import com.otaviosenger.financial.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> { // INTEGER = ID
}

// METHODS FOR WORK WITH THE DATABASE PROVIDED BY SPRING DATA JPA