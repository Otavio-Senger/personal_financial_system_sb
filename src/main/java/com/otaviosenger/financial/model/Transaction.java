package com.otaviosenger.financial.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id  //PRIMARY KEY
    @GeneratedValue(strategy = GenerationType.IDENTITY) //MEANS THE MySQL GENERATES THE ID
    private Integer id;

    @NotBlank(message = "Description is required")
    @Size(max = 100, message = "Description must have at most 100 characters")
    @Column(name = "descriptions", nullable = false, length = 100)
    private String description;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount ,must be greater than zero")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @NotNull(message = "Date is required")
    @Column(name = "dates", nullable = false)
    private LocalDate date;

    @NotNull(message = "Category is required")
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    //CONSTRUCTORS

    public Transaction() {
    }

    public Transaction(Integer id, String description, BigDecimal amount, LocalDate date, Category category) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.category = category;
    }

    //GETTERS, SETTERS AND ToSTRING


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                ", category=" + category +
                '}';
    }
}
