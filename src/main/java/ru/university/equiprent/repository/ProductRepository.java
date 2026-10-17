package ru.university.equiprent.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ru.university.equiprent.model.Product;
import ru.university.equiprent.service.IdGeneratorService;

@RequiredArgsConstructor
@Repository
public class ProductRepository implements ProductDao {
    private final IdGeneratorService idGeneratorService;
    private final Map<Long, Product> storage = new ConcurrentHashMap<>();

    @Override
    public Product save(Product product) {
        if (product.getId() == null) {
            product.setId(idGeneratorService.nextProductId());
        }
        storage.put(product.getId(), product);
        return product;
    }

    @Override
    public Product update(Product product) {
        storage.put(product.getId(), product);
        return product;
    }

    @Override public List<Product> findAll() { return List.copyOf(storage.values()); }
    @Override public Optional<Product> findById(Long id) { return Optional.ofNullable(storage.get(id)); }
    @Override public void delete(Long id) { storage.remove(id); }
}