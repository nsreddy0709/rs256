package com.example.rs256.repository;

import com.example.rs256.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users,Integer> {
    Users findUsersByEmailAndPassword(String email,String password);
    Users findUsersByEmail(String email);
    Users findByEmail(String email);
}
