package com.example.rs256.repository;


import com.example.rs256.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders,Integer> {
    List<Orders> findOrdersByUid(Integer id);
}
