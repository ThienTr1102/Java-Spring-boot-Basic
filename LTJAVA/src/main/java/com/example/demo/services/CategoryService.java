package com.example.demo.services;

import com.example.demo.entity.Category;
import com.example.demo.repository.ICategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private ICategoryRepository categoryRepository;

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id){
        Optional<Category> optional = categoryRepository.findById(id);
        return optional.orElse( null);
    }

    public void addCategory(Category book){

        categoryRepository.save(book);
    }

    public void updateBook(Category book){
        categoryRepository.save(book);
    }

    public void deleteBook(Long id){
        categoryRepository.deleteById(id);
    }
}
