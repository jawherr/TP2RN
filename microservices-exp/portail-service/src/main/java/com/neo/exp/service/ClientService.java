package com.neo.exp.service;

import com.neo.exp.dto.OrgDTO;
import com.neo.exp.entity.Product;

import java.util.Optional;

public interface ClientService {
    String register(OrgDTO orgDTO);
    Optional<Product> getProduct(String productId);
}
