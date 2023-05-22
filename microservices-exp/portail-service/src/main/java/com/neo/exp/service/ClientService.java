package com.neo.exp.service;

import com.neo.exp.dto.OrgDto;

import java.util.Optional;

public interface ClientService {
    String register(OrgDto orgDTO);
    //Optional<Product> getProduct(String productId);
}
