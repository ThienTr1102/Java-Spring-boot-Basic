package com.example.demo.repository;

import com.example.demo.entity.Book;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IBookRepository extends JpaRepository<Book, Long> {

    default List<Book> findAllBooks(Integer pageNo,
                                    Integer pageSize,
                                    String sortBy){
        return findAll(PageRequest.of(pageNo,
                pageSize,
                Sort.by(sortBy)))
                .getContent();
    }
    //Viet cau query like
    @Query("""
            SELECT b
            FROM Book b
            WHERE b.title LIKE %?1%
                OR b.author LIKE %?1%
                OR b.category.name LIKE %?1%
            """)
    List<Book> searchBook(String keyword);
}

