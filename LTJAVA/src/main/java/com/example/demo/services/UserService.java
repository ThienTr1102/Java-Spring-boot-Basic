package com.example.demo.services;


import com.example.demo.entity.User;

import com.example.demo.repository.IRoleRepository;
import com.example.demo.repository.IUserRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    public void save(User user) {
        userRepository.save(user);
        Long userId = userRepository.getUserIdByUsername(user.getUsername());
        Long roleId = roleRepository.getRoleIdByName("USER");
        if (roleId != 0 && userId != 0) {
            userRepository.addRoleToUser(userId, roleId);
        }
    }
    public void saveOauthUser(String email, @NotNull String username){
        if(userRepository.findByUsername(username)!= null)
            return;
        var user = new User();
        user.setName(username);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(new BCryptPasswordEncoder().encode(username));
        userRepository.save(user);

        Long userId = userRepository.getUserIdByUsername(user.getUsername());
        Long roleId = roleRepository.getRoleIdByName("USER");
        if (roleId != 0 && userId != 0) {
            userRepository.addRoleToUser(userId, roleId);
        }
    }
}

