package ru.university.equiprent.repository;

import java.util.List;
import java.util.Optional;

import ru.university.equiprent.model.Product;

public interface ProductDao {
    Product save(Product product);
    Product update(Product product);
    List<Product> findAll();
    Optional<Product> findById(Long id);
    void delete(Long id);
}