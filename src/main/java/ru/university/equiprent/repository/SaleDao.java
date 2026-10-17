package ru.university.equiprent.repository;

import java.util.List;
import java.util.Optional;

import ru.university.equiprent.model.Sale;

public interface SaleDao {
    Sale save(Sale sale);
    List<Sale> findAll();
    Optional<Sale> findById(Long id);
    void delete(Long id);
}