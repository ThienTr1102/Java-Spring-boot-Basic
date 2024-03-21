package com.example.demo.services;

import com.example.demo.entity.Book;
import com.example.demo.entity.Invoice;
import com.example.demo.repository.IInvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {
    @Autowired
    private IInvoiceRepository invoiceRepository;

    public List<Invoice> getAllInvoices(){
        return invoiceRepository.findAll();
    }

    public List<Invoice> getAllInvoices(Integer pageNo, Integer pageSize, String sortBy){
        return invoiceRepository.findAllInvoices(pageNo, pageSize, sortBy);
    }

    public Invoice getInvoiceById(Long id){
        Optional<Invoice> optional = invoiceRepository.findById(id);
        return optional.orElse( null);
    }

    @PreAuthorize("hasAnyAuthority('USER') or hasAnyAuthority('ADMIN')")
    public List<Invoice> searchInvoice(String keyword){
        return invoiceRepository.searchInvoice(keyword);
    }
}
