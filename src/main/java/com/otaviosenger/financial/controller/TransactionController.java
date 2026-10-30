package com.otaviosenger.financial.controller;


import com.otaviosenger.financial.model.Transaction;
import com.otaviosenger.financial.model.Type;
import com.otaviosenger.financial.repository.TransactionRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionRepository transactionRepository;

    public TransactionController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @GetMapping
    public List<Transaction> list(@RequestParam(required = false) Type type,
                                  @RequestParam(required = false) Integer categoryId,
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
    public Transaction create(@RequestBody Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        transactionRepository.deleteById(id);
    }


}
