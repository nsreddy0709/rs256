package com.example.rs256.repository;


import com.example.rs256.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface CartRepository extends JpaRepository<Cart,Integer> {



    Cart findCartByUidAndPid(Integer id1,Integer id2);

    List<Cart> findCartByUid(Integer id);

    List<Cart> findCartByPid(Integer id);

    void deleteCartByUidAndPid(Integer id1,Integer id2);

    void deleteCartByUid(Integer id);


}
