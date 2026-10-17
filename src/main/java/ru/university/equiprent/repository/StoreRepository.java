package ru.university.equiprent.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ru.university.equiprent.model.Store;
import ru.university.equiprent.service.IdGeneratorService;

@RequiredArgsConstructor
@Repository
public class StoreRepository implements StoreDao {
    private final IdGeneratorService idGeneratorService;
    private final Map<Long, Store> storage = new ConcurrentHashMap<>();

    @Override
    public Store save(Store store) {
        if (store.getId() == null) {
            store.setId(idGeneratorService.nextStoreId());
        }
        storage.put(store.getId(), store);
        return store;
    }

    @Override public List<Store> findAll() { return List.copyOf(storage.values()); }
    @Override public Optional<Store> findById(Long id) { return Optional.ofNullable(storage.get(id)); }
    @Override public void delete(Long id) { storage.remove(id); }
}