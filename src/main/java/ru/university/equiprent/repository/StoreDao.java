package ru.university.equiprent.repository;

import java.util.List;
import java.util.Optional;

import ru.university.equiprent.model.Store;

public interface StoreDao {
    Store save(Store store);
    List<Store> findAll();
    Optional<Store> findById(Long id);
    void delete(Long id);
}