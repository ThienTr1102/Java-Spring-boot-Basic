package com.example.demo.repository;

import com.example.demo.entity.Invoice;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IInvoiceRepository extends JpaRepository<Invoice,Long> {
    default List<Invoice> findAllInvoices(Integer pageNo,
                                          Integer pageSize,
                                          String sortBy){
        return findAll(PageRequest.of(pageNo,
                pageSize,
                Sort.by(sortBy))).getContent();
    }

    //luu y o day mình thực hiện phép toán join với bảng user
    //cac đối tượng thực hiệ trong query phải ánh xạ khớp vs class entity mà khai báo
    @Query("""
            SELECT i 
            FROM Invoice i 
                LEFT JOIN user u on u.id = i.user.id
            WHERE u.name LIKE %?1%
                OR u.email LIKE %?1%
            """)
    List<Invoice> searchInvoice(String keyword);
}