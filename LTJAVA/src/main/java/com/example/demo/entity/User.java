package com.example.demo.entity;

import com.example.demo.validator.annotation.ValidUsername;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//Tu dong phat sinh thu vien lumbok
@Data
//dung thu vien de chi dinh doi tuong mapping
@Entity
//Tuong ung voi bang trong csdl book
@Table(name = "user")
public class User {
    //Khai bao khoa chinh id tu tang GenerationType.IDENTITY
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Khai cao column ten username voi chieu dai toi da 50 ki tu
    //Cot nay khong cho phep gia tri null
    //Va cac gia tri username la duy nhat, khong trung
    @Column(name = "username", length = 50, nullable = false, unique = true)
    //Noi dung thong diep tra ve khi truong nay bi rong
    @NotBlank(message = "Username is required")
    //Chuoi ki tu toi da 50, qua thi hien thong bao nay
    @Size(max = 50, message = "Username must be less than 50 characters")
    //Annotation do minh tu dinh nghia
    @ValidUsername
    private String username;

    //Khai bao cot password, chieu dai toi da 250 ki tu, khong duoc phep nhan gia tri null
    @Column(name = "password", length = 250, nullable = false)
    //Truong hop rong thi hien thi thong diep nay
    @NotBlank(message = "Password is required")
    private String password;

    @Column(name = "email", length = 50)
    @Size(max = 50, message = "Email must be less than 50 characters")
    private String email;

    @Column(name = "name", length = 50, nullable = false)
    @Size(max = 50, message = "Your name must be less than 50 characters")
    @NotBlank(message = "Your name is required")
    private String name;



    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Book> books = new ArrayList<>();


    @ManyToMany
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
}