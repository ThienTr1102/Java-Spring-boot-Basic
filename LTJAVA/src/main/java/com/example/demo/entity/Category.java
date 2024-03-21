package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;
@Data
@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotEmpty(message = "Name must not be empty")
    @Size(max = 50, min =1)
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Book> books;
}
