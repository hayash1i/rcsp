package ru.university.equiprent.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ru.university.equiprent.model.Sale;
import ru.university.equiprent.service.IdGeneratorService;

@RequiredArgsConstructor
@Repository
public class SaleRepository implements SaleDao {
    private final IdGeneratorService idGeneratorService;
    private final Map<Long, Sale> storage = new ConcurrentHashMap<>();

    @Override
    public Sale save(Sale sale) {
        if (sale.getId() == null) {
            sale.setId(idGeneratorService.nextSaleId());
        }
        storage.put(sale.getId(), sale);
        return sale;
    }

    @Override public List<Sale> findAll() { return List.copyOf(storage.values()); }
    @Override public Optional<Sale> findById(Long id) { return Optional.ofNullable(storage.get(id)); }
    @Override public void delete(Long id) { storage.remove(id); }
}