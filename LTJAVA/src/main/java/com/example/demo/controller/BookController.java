package com.example.demo.controller;


import com.example.demo.daos.Item;
import com.example.demo.entity.Book;
import com.example.demo.repository.IBookRepository;
import com.example.demo.services.BookService;
import com.example.demo.services.CartService;
import com.example.demo.services.CategoryService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private IBookRepository bookrepository;

//    @GetMapping
//    public String showAllBooks(Model model){
//        List<Book> books = bookService.getAllBooks();
//        model.addAttribute("books", books);
//        return "book/list";
//    }

    @GetMapping
    public String showAllBook(
            @NotNull Model model,
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        model.addAttribute("title", "Book List");
        model.addAttribute("books", bookService.getAllBooks(pageNo, pageSize, sortBy));
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", bookService.getAllBooks().size() / pageSize);
        return "book/list";
    }

    //them moi tra ve giao dien hien thi
    //xu ly requuest domain/book/add
    @GetMapping("/add")
    public String addBookForm(Model model) {
        //truyen 2 tham so sang view
        model.addAttribute("book", new Book());
        //truyen ds categr hien thi
        model.addAttribute("categories", categoryService.getAllCategories());
        //goi layer de render giao dien
        return "book/add";
    }

    @PostMapping("/add")
    public String addBook(@Valid @ModelAttribute("book") Book book,
                          BindingResult bindingResult,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", book);
            model.addAttribute("categories",
                    categoryService.getAllCategories());

        } else {
            bookService.addBook(book);
            return "redirect:/books";
        }
        return "book/add";
    }

    @GetMapping("/edit/{id}")
    public String editBookForm(@PathVariable("id") Long id, Model model) {
        //lay thong tin tu csdl cua quyen sach tu csdl ve thong qua bookser by ID
        //gan thong tin quyen sach do va bien book de truyen qua view
        model.addAttribute("book", bookService.getBookById(id));
        model.addAttribute("categories", categoryService.getAllCategories());
        return "book/edit";
    }

    // thuc thi qua trinh cap nhat khi user bam nut save tren giao dien
    //link theo phuong thuc post
    //du lieu nguoi dung thay doi se duoc luu o bien book ben view
    // gan vao bien ten updatebook cho ham
    @PostMapping("/edit")
    public String editBook(@ModelAttribute("book") Book updateBook) {
        //tien hanh goi service de update thong tin vao csdl
        //addbook se tien hanh tao moi neu k co id nguoc lai thi tien hanh cap nhat
        bookService.updateBook(updateBook);
        return "redirect:/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }

    //capture request demain/books/search voi keyword la tu khoa
    @GetMapping("/search")
    public String searchBook(@NotNull Model model,
                             @RequestParam String keyword,
                             @RequestParam(defaultValue = "0") Integer pageNo,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             @RequestParam(defaultValue = "id") String sortBy) {
        model.addAttribute("books", bookService.searchBook(keyword));
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", bookService.searchBook(keyword).size() / pageSize);
        return "book/list";
    }

    @Autowired
    private CartService cartService;

    @PostMapping("/add-to-cart")
    public String addToCart(HttpSession session,
                            @RequestParam Long id,
                            @RequestParam String name,
                            @RequestParam double price,
                            @RequestParam(defaultValue = "1") int quantity) {
        var cart = cartService.getCart(session);
        cart.addItems(new Item(id, name, price, quantity));
        cartService.updateCart(session, cart);
        return "redirect:/books";
    }
}
