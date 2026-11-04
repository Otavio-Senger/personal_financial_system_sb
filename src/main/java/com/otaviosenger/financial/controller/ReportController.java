package com.otaviosenger.financial.controller;

import com.otaviosenger.financial.model.Type;
import com.otaviosenger.financial.repository.TransactionRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final TransactionRepository transactionRepository;

    public ReportController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/inflow-outflow")
    public Map<String, BigDecimal> inflowAndOutflow() {
        BigDecimal revenue = Objects.requireNonNullElse(
                transactionRepository.sumByType(Type.REVENUE), BigDecimal.ZERO);
        BigDecimal expenses = Objects.requireNonNullElse(
                transactionRepository.sumByType(Type.EXPENSES), BigDecimal.ZERO);

        return Map.of(
                "totalRevenue", revenue,
                "totalExpenses", expenses,
                "balance", revenue.subtract(expenses));
    }
}
