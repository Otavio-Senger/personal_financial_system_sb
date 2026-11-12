package com.otaviosenger.financial.controller;


import com.otaviosenger.financial.exception.ResourceNotFoundException;
import com.otaviosenger.financial.model.Category;
import com.otaviosenger.financial.repository.CategoryRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category create(@Valid @RequestBody Category category) {
        return categoryRepository.save(category);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category " + id + " not found");
        }
        categoryRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public Category update(@PathVariable Integer id, @Valid @RequestBody Category category) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category " + id + " not found");
        }
        category.setId(id);
        return categoryRepository.save(category);
    }

}
