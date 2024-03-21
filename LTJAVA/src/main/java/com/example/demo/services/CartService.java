package com.example.demo.services;

import com.example.demo.daos.Cart;
import com.example.demo.daos.Item;
import com.example.demo.entity.CustomUserDetail;
import com.example.demo.entity.Invoice;
import com.example.demo.entity.ItemInvoice;
import com.example.demo.repository.IBookRepository;
import com.example.demo.repository.IInvoiceRepository;
import com.example.demo.repository.IItemInvoiceRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final IItemInvoiceRepository iItemInvoiceRepository;
    private final IBookRepository bookRepository;
    private final IInvoiceRepository invoiceRepository;
    private static final String CART_SESSION_KEY = "giohang";

    @PreAuthorize("hasAnyAuthority('USER') or hasAnyAuthority('ADMIN')")
    public void saveCart(@NotNull HttpSession session){
        var cart = getCart(session);
        if(cart.getCartItems().isEmpty())
            return;
        var invoice = new Invoice();
        invoice.setInvoiceDate(new Date(new Date().getTime()));
        invoice.setPrice(getSumPrice(session));
        invoice.setUser(
                ((CustomUserDetail)SecurityContextHolder.getContext().
                        getAuthentication().getPrincipal()).getUser());
        invoiceRepository.save(invoice);

        //tien hanh them chi tet san pham trong hoa don
        //dung vong lap de quet so luong san pham trong cart
        //mot san pham trong cart se luu vao bien item
        cart.getCartItems().forEach(item -> {
            //tao chi viet don hang iteminvoice
            var items = new ItemInvoice();
            items.setInvoice(invoice);
            //set hoa don tong
            items.setQuantity(item.getQuantity());
            //set so luong cua item san pham
            items.setBook(bookRepository.findById(item.getBookId()).orElseThrow());
            //tien hanh luu vao csdl
            iItemInvoiceRepository.save(items);
        });
        //xoa gio hang di
        removeCart(session);
    }

    @PreAuthorize("hasAnyAuthority('USER') or hasAnyAuthority('ADMIN')")
    public Cart getCart(@NotNull HttpSession session){
        return Optional.ofNullable(
                (Cart) session.getAttribute(CART_SESSION_KEY))
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    session.setAttribute(CART_SESSION_KEY, cart);
                    return cart;
                });
    }

    @PreAuthorize("hasAnyAuthority('USER') or hasAnyAuthority('ADMIN')")
    public void updateCart(@NotNull HttpSession session, Cart cart){
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    public void removeCart(@NotNull HttpSession session){
        session.removeAttribute((CART_SESSION_KEY));
    }

    @PreAuthorize("hasAnyAuthority('USER') or hasAnyAuthority('ADMIN')")
    public int getSumQuantity(@NotNull HttpSession session){
        return getCart(session).getCartItems()
                .stream()
                .mapToInt(Item::getQuantity)
                .sum();
    }
    @PreAuthorize("hasAnyAuthority('USER') or hasAnyAuthority('ADMIN')")
    public double getSumPrice(@NotNull HttpSession session){
        return getCart(session).getCartItems()
                .stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}
