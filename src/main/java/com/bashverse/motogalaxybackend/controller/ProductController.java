package com.bashverse.motogalaxybackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
public class ProductController {

    @GetMapping("/api/products")
    public List<Map<String, Object>> getProducts() {
        return List.of(
                Map.of("id", 1, "name", "Motorcycle Helmet", "price", 79.99, "description", "High-quality helmet for safety"),
                Map.of("id", 2, "name", "Engine Oil", "price", 24.99, "description", "Premium engine oil for better performance"),
                Map.of("id", 3, "name", "Brake Pads", "price", 39.99, "description", "Durable brake pads for smooth riding")
        );
    }
}

