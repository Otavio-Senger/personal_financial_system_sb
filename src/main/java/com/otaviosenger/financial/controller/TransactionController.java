package com.otaviosenger.financial.controller;


import com.otaviosenger.financial.exception.ResourceNotFoundException;
import com.otaviosenger.financial.model.Category;
import com.otaviosenger.financial.model.Transaction;
import com.otaviosenger.financial.model.Type;
import com.otaviosenger.financial.repository.CategoryRepository;
import com.otaviosenger.financial.repository.TransactionRepository;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/transactions") // ENDPOINTS START WITH /transactions
public class TransactionController {
    private final TransactionRepository transactionRepository; //TransactionRepository METHODS
    private final CategoryRepository categoryRepository; //CategoryRepository METHODS

    public TransactionController(TransactionRepository transactionRepository, CategoryRepository categoryRepository) { // CONSTRUCTOR INJECTION
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

    private void attachCategory(Transaction transaction) {
        Integer categoryId = transaction.getCategory().getId();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category " + categoryId + " not found")); // JUST WHEN THE CATEGORY IS NOT FOUND
        transaction.setCategory(category); // RELATES THE TRANSACTION TO THE CATEGORY FOUND IN THE DATABASE
    }

    @GetMapping
    public List<Transaction> list(@RequestParam(required = false) Type type, // QUERY PARAMETERS -> GET /transactions?type=EXPENSE
                                  @RequestParam(required = false) Integer categoryId, // GET /transactions?categoryId=1
                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        if (startDate != null && endDate != null) {
            return transactionRepository.findByDateBetween(startDate, endDate);
        }
        if (categoryId != null) {
            return transactionRepository.findByCategory_Id(categoryId);
        }
        if (type != null) {
            return transactionRepository.findByCategory_Type(type);
        }
        return transactionRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction create(@Valid @RequestBody Transaction transaction) { // REQUEST BODY: TRANSFORM A JSON INTO A JAVA OBJECT (Transaction transaction)
        attachCategory(transaction); // RELATES THE TRANSACTION TO THE CATEGORY FOUND IN THE DATABASE
        return transactionRepository.save(transaction);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) { // PATH VARIABLE: ONLY ID AS A PARAMETER
        if (!transactionRepository.existsById(id)){
            throw new ResourceNotFoundException("transaction " + id + " not found"); // ONLY IF THE ID TRANSACTION IS NOT FOUND
        }
        transactionRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public Transaction update(@PathVariable Integer id, @Valid @RequestBody Transaction transaction) {
        if (!transactionRepository.existsById(id)) {
            throw new ResourceNotFoundException("transaction " + id + " not found");
        }
        attachCategory(transaction); // RELATES THE TRANSACTION TO THE CATEGORY FOUND IN THE DATABASE
        transaction.setId(id); // JSON DOESN'T SEND THE TRANSACTION ID TO THE JAVA OBJECT, SO THIS LINE MAKE THIS
        return transactionRepository.save(transaction);
    }


}
