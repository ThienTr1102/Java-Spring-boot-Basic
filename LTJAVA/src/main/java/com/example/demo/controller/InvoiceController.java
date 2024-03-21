package com.example.demo.controller;

import com.example.demo.services.InvoiceService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/invoice")
public class InvoiceController {
    @Autowired
    private InvoiceService invoiceService;

    @GetMapping
    public String showAllInvoice(@NotNull Model model,
                                 @RequestParam(defaultValue = "0") Integer pageNo,
                                 @RequestParam(defaultValue = "3") Integer pageSize,
                                 @RequestParam(defaultValue = "id") String sortBy)
    {
        model.addAttribute("invoices", invoiceService.getAllInvoices(pageNo, pageSize, sortBy));
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", invoiceService.getAllInvoices().size()/pageSize);
        return "invoice/list";
    }

    @GetMapping("/search")
    public String searchInvoices(@NotNull Model model, @RequestParam String keyword,
                                 @RequestParam(defaultValue = "0") Integer pageNo,
                                 @RequestParam(defaultValue = "5") Integer pageSize,
                                 @RequestParam(defaultValue = "id") String sortBy) {
        model.addAttribute("invoices", invoiceService.searchInvoice(keyword));
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", invoiceService.searchInvoice(keyword).size()/pageSize);
        return "invoice/list";
    }

    @GetMapping("/detail/{id}")
    public String detailInvoice(@PathVariable("id") Long id, Model model){
        model.addAttribute("invoice", invoiceService.getInvoiceById(id));
        return "invoice/detail";
    }
}