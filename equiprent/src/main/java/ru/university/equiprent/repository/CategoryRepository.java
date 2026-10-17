package ru.university.equiprent.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ru.university.equiprent.model.Category;
import ru.university.equiprent.service.IdGeneratorService;

@RequiredArgsConstructor
@Repository
public class CategoryRepository implements CategoryDao {
    private final IdGeneratorService idGeneratorService;
    private final Map<Long, Category> storage = new ConcurrentHashMap<>();

    @Override
    public Category save(Category category) {
        if (category.getId() == null) {
            category.setId(idGeneratorService.nextCategoryId());
        }
        storage.put(category.getId(), category);
        return category;
    }

    @Override public List<Category> findAll() { return List.copyOf(storage.values()); }
    @Override public Optional<Category> findById(Long id) { return Optional.ofNullable(storage.get(id)); }
    @Override public void delete(Long id) { storage.remove(id); }
}