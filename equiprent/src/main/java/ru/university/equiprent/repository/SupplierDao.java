package ru.university.equiprent.repository;

import java.util.List;
import java.util.Optional;

import ru.university.equiprent.model.Supplier;

public interface SupplierDao {
    Supplier save(Supplier supplier);
    List<Supplier> findAll();
    Optional<Supplier> findById(Long id);
    void delete(Long id);
}