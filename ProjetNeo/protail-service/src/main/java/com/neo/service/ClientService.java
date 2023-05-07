package com.neo.service;

import com.neo.dto.OrgDTO;
import com.neo.entity.Product;

import java.util.Optional;

public interface ClientService {
    String register(OrgDTO orgDTO);
    Optional<Product> getProduct(String productId);
}
