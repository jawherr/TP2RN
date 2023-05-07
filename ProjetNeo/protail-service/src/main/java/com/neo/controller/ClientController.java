package com.neo.controller;

import com.neo.dto.OrgDTO;
import com.neo.entity.Product;
import com.neo.multitenancy.TenantContextHolder;
import com.neo.repository.ProductRepository;
import com.neo.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class ClientController {

    @Inject
    private ClientService clientService;
    @Inject
    private ProductRepository productRepository;

    @PostMapping("/registerOrg")
    public String registerOrg(@RequestBody OrgDTO orgDTO)
    {
        return clientService.register(orgDTO);
    }

    @PostMapping("/getProducts/{tenant}")
    public List<Product> getProducts(HttpServletRequest request, @PathVariable("tenant") String tenant){
        TenantContextHolder.setTenantId(tenant);
        return productRepository.findAll();
    }

    @GetMapping("/produit")
    public Optional<Product> getproduct(){
        return clientService.getProduct("hey");
    }

    private String obtainTenantFromSubdomain(HttpServletRequest request) {
        return request.getServerName().split("\\.")[0];
    }
}
