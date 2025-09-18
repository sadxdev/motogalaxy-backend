package com.bashverse.motogalaxybackend.repository;

import com.bashverse.motogalaxybackend.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

	 Cart findByUserId(Long userId);
}
