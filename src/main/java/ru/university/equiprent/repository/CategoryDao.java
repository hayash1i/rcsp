package ru.university.equiprent.repository;

import java.util.List;
import java.util.Optional;

import ru.university.equiprent.model.Category;

public interface CategoryDao {
    Category save(Category category);
    List<Category> findAll();
    Optional<Category> findById(Long id);
    void delete(Long id);
}