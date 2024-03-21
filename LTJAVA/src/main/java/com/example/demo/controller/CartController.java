package com.example.demo.controller;

import com.example.demo.services.CartService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public String showCart(HttpSession session,
                           @NotNull Model model) {
        model.addAttribute("cart", cartService.getCart(session)); //lấy thông tin của giỏ hàng truyền vào biến cart để xử lý ở View
        model.addAttribute("totalPrice",cartService.getSumPrice(session)); //trả về tổng giá trị của giỏ hàng vào biến totalPrice
        model.addAttribute("totalQuantity",cartService.getSumQuantity(session)); //trả về tổng giá trị của giỏ hàng vào biến totalQuantity
        return "book/cart"; //--> giao diện templates/books/cart.html
    }

    @GetMapping("/updatecart/{id}/{quantity}")
    public String updateCart(HttpSession session, @PathVariable Long id, @PathVariable int quantity){
        var cart = cartService.getCart(session);
        cart.updateItems(id, quantity);
        return "redirect:/cart";
    }

    @GetMapping("/removefromcart/{id}")
    public String removeFromCart(HttpSession session, @PathVariable Long id) {
        var cart = cartService.getCart(session);
        cart.removeItems(id);
        return "redirect:/cart";
    }

    @GetMapping("clearcart")
    public String clearCart(HttpSession session){
        cartService.removeCart(session);
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    private  String checkout(HttpSession session){
        cartService.saveCart(session);
        return "redirect:/cart";
    }
}