package com.bashverse.motogalaxybackend.repository;

import com.bashverse.motogalaxybackend.model.Cart;
import com.bashverse.motogalaxybackend.model.CartItem;
import com.bashverse.motogalaxybackend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {


    CartItem findByCartAndProductAndSize(Cart cart, Product product, String size);


}
