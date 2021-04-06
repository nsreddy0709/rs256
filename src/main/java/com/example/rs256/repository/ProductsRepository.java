package com.example.rs256.repository;


import com.example.rs256.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository extends JpaRepository<Products,Integer> {
    Products findProductsByPtitle(String title);
}
