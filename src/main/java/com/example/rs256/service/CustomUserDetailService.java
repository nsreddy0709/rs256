package com.example.rs256.service;

import com.example.rs256.model.Users;
import com.example.rs256.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailService {

    @Autowired
    private UsersRepository usersRepository;


    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Users user = usersRepository.findByEmail(email);

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());

    }
}
