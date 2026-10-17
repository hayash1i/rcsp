package ru.university.equiprent.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ru.university.equiprent.model.Supplier;
import ru.university.equiprent.service.IdGeneratorService;

@RequiredArgsConstructor
@Repository
public class SupplierRepository implements SupplierDao {
    private final IdGeneratorService idGeneratorService;
    private final Map<Long, Supplier> storage = new ConcurrentHashMap<>();

    @Override
    public Supplier save(Supplier supplier) {
        if (supplier.getId() == null) {
            supplier.setId(idGeneratorService.nextSupplierId());
        }
        storage.put(supplier.getId(), supplier);
        return supplier;
    }

    @Override public List<Supplier> findAll() { return List.copyOf(storage.values()); }
    @Override public Optional<Supplier> findById(Long id) { return Optional.ofNullable(storage.get(id)); }
    @Override public void delete(Long id) { storage.remove(id); }
}