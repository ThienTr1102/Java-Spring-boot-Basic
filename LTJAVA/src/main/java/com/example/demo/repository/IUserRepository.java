package com.example.demo.repository;


import com.example.demo.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface IUserRepository extends JpaRepository<User, Long> {

    //viet cau query cho ham, va cac tham so se thay tge vao tuong ung
    //username thay vao cho ?1
    @Query("SELECT u FROM User u WHERE u.username = ?1")
    User findByUsername(String username);


    //viet ham insert role vao bang user_role
    //Bien userid sẽ thế vào ?1
    //Biến roldID sẽ thế vào ?2
    //lab 7
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user_role (user_id, role_id) " +
            "VALUES (?1, ?2)",
            nativeQuery = true)
    void addRoleToUser(Long userId, Long roleId);

    //viết hàm lấy id của username
    @Query("SELECT u.id FROM User u WHERE u.username = ?1")
    Long getUserIdByUsername(String username);

    //tên của các role của 1 user: các quyền của userId
    @Query(value = "SELECT r.name " +
            "FROM role r " +
            "INNER JOIN user_role ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = ?1",
            nativeQuery = true)
    String[] getRolesOfUser(Long userId);


}
