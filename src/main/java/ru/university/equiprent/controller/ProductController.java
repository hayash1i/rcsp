package ru.university.equiprent.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import ru.university.equiprent.api.ProductApi;
import ru.university.equiprent.dto.ProductRequest;
import ru.university.equiprent.dto.ProductResponse;
import ru.university.equiprent.service.ProductService;

@RestController
public class ProductController implements ProductApi {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public List<ProductResponse> getAll(Long category, Long supplier, boolean lowStock) {
        return productService.findAll(category, supplier, lowStock);
    }

    @Override
    public ProductResponse get(Long id) {
        return productService.findById(id);
    }

    @Override
    public ProductResponse create(ProductRequest request) {
        return productService.create(request);
    }

    @Override
    public ProductResponse update(Long id, ProductRequest request) {
        return productService.update(id, request);
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}