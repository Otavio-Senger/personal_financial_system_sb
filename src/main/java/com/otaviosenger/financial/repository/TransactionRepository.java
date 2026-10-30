package com.otaviosenger.financial.repository;

import com.otaviosenger.financial.model.Category;
import com.otaviosenger.financial.model.Type;
import com.otaviosenger.financial.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findByCategory_Id(Integer category);
    List<Transaction> findByCategory_Type(Type type);
    List<Transaction> findByDateBetween(LocalDate startDate, LocalDate endDate);

}
