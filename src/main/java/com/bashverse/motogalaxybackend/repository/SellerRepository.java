package com.bashverse.motogalaxybackend.repository;

import com.bashverse.motogalaxybackend.domain.AccountStatus;
import com.bashverse.motogalaxybackend.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    Seller findByEmail(String email);
    List<Seller> findByAccountStatus(AccountStatus status);
}
