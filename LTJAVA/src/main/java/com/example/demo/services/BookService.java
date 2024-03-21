package com.example.demo.services;

import com.example.demo.entity.Book;
import com.example.demo.repository.IBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private IBookRepository bookRepository;

    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    public List<Book> getAllBooks(Integer pageNo,
                                  Integer pageSize,
                                  String sortBy){
        return bookRepository.findAllBooks(pageNo, pageSize, sortBy);
    }
    public void softDeleteBook(Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            book.setDeleted(true);
            bookRepository.save(book);
        }
    }

    public Book getBookById(Long id){
        Optional<Book> optional = bookRepository.findById(id);
        return optional.orElse( null);
    }

    public void addBook(Book book){

        bookRepository.save(book);
    }

    public void updateBook(Book book){
        bookRepository.save(book);
    }

    public void deleteBook(Long id){
        bookRepository.deleteById(id);
    }

    @PreAuthorize("hasAnyAuthority('USER') or hasAnyAuthority('ADMIN')")
    public List<Book> searchBook(String keyword){
        return bookRepository.searchBook(keyword);
    }
}
