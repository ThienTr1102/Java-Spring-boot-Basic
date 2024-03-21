package com.example.demo.entity;
import com.example.demo.validator.annotation.ValidUserId;
import com.example.demo.validator.annotation.ValidCategoryId;
import com.example.demo.validator.annotation.ValidUsername;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Data
@Entity
@Table(name="book")
@SQLDelete( sql = "UPDATE book SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Book {
    private boolean deleted;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    @NotEmpty(message = "Title must not be empty")
    @Size(max = 50, min =1)
    private String title;

    @Column(name = "author")
    private String author;


    @Column(name = "price")
    @NotNull(message = "Price is required")
    private Double price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @ValidCategoryId
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    @ValidUserId
    private User user;
}
