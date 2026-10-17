package ru.university.equiprent.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.university.equiprent.dto.SaleItemRequest;
import ru.university.equiprent.dto.SaleItemResponse;
import ru.university.equiprent.dto.SaleRequest;
import ru.university.equiprent.dto.SaleResponse;
import ru.university.equiprent.exception.InsufficientStockException;
import ru.university.equiprent.exception.NotFoundException;
import ru.university.equiprent.model.Product;
import ru.university.equiprent.model.Sale;
import ru.university.equiprent.model.SaleItem;
import ru.university.equiprent.model.Store;
import ru.university.equiprent.model.User;
import ru.university.equiprent.repository.ProductRepository;
import ru.university.equiprent.repository.SaleRepository;
import ru.university.equiprent.repository.StoreRepository;
import ru.university.equiprent.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final IdGeneratorService idGeneratorService;

    /**
     * Регистрация продажи. Транзакционно: сначала валидируем весь заказ,
     * затем списываем остатки. При нехватке — откат (ничего не списываем).
     */
    public synchronized SaleResponse registerSale(SaleRequest request) {
        Store store = storeRepository.findById(request.storeId())
                .orElseThrow(() -> new NotFoundException("Store not found: " + request.storeId()));
        User cashier = userRepository.findById(request.cashierId())
                .orElseThrow(() -> new NotFoundException("User not found: " + request.cashierId()));

        // 1. Валидация всех позиций до списания
        List<SaleItem> items = new ArrayList<>();
        for (SaleItemRequest itemReq : request.items()) {
            Product product = productRepository.findById(itemReq.productId())
                    .orElseThrow(() -> new NotFoundException(
                            "Product not found: " + itemReq.productId()));
            if (product.getStockQuantity() < itemReq.quantity()) {
                throw new InsufficientStockException(String.format(
                        "Insufficient stock for product '%s' (id=%d): available=%d, requested=%d",
                        product.getTitle(), product.getId(),
                        product.getStockQuantity(), itemReq.quantity()));
            }
            items.add(SaleItem.builder()
                    .id(idGeneratorService.nextSaleItemId())
                    .product(product)
                    .quantity(itemReq.quantity())
                    .priceAtSaleTime(product.getPrice())
                    .build());
        }

        // 2. Списание остатков (после успешной валидации)
        for (SaleItem item : items) {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.update(product);
        }

        // 3. Сохранение продажи
        Sale sale = Sale.builder()
                .store(store)
                .cashier(cashier)
                .items(items)
                .soldAt(LocalDateTime.now())
                .build();
        saleRepository.save(sale);

        return toResponse(sale);
    }

    public List<SaleResponse> findAll(Long storeId, LocalDateTime from, LocalDateTime to) {
        return saleRepository.findAll().stream()
                .filter(s -> storeId == null
                        || (s.getStore() != null && s.getStore().getId().equals(storeId)))
                .filter(s -> from == null || !s.getSoldAt().isBefore(from))
                .filter(s -> to == null || !s.getSoldAt().isAfter(to))
                .sorted(Comparator.comparing(Sale::getSoldAt).reversed())
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public SaleResponse findById(Long id) {
        return toResponse(saleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sale not found: " + id)));
    }

    private SaleResponse toResponse(Sale s) {
        List<SaleItemResponse> itemResponses = s.getItems().stream()
                .map(i -> new SaleItemResponse(
                        i.getId(),
                        i.getProduct().getId(),
                        i.getProduct().getTitle(),
                        i.getQuantity(),
                        i.getPriceAtSaleTime(),
                        i.getQuantity() * i.getPriceAtSaleTime()))
                .toList();
        int total = itemResponses.stream().mapToInt(SaleItemResponse::lineTotal).sum();
        return new SaleResponse(
                s.getId(),
                s.getStore().getId(),
                s.getStore().getAddress(),
                s.getCashier().getId(),
                s.getCashier().getFullName(),
                s.getSoldAt(),
                itemResponses,
                total);
    }
}