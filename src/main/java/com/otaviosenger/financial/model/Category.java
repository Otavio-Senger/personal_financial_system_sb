package com.otaviosenger.financial.model;

import jakarta.persistence.*;

@Entity
@Table(name="categories")
public class Category {

    @Id //PRIMARY KEY
    @GeneratedValue(strategy = GenerationType.IDENTITY) //MEANS MySQL GENERATES THE ID
    private Integer id;

    @Column(nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING) //SAVES THE ENUM NAME AS TEXT
    @Column(nullable = false)
    private Type type;

    //CONSTRUCTORS
    public Category(){}
    public Category(Integer id, String name, Type type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    //GETTERS, SETTERS AND ToString

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
