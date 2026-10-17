package ru.university.equiprent.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.university.equiprent.dto.ProductRequest;
import ru.university.equiprent.dto.ProductResponse;
import ru.university.equiprent.exception.NotFoundException;
import ru.university.equiprent.model.Category;
import ru.university.equiprent.model.Product;
import ru.university.equiprent.model.Supplier;
import ru.university.equiprent.repository.CategoryRepository;
import ru.university.equiprent.repository.ProductRepository;
import ru.university.equiprent.repository.SupplierRepository;

@Service
@RequiredArgsConstructor
public class ProductService {

    public static final int LOW_STOCK_THRESHOLD = 5;

    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;

    public List<ProductResponse> findAll(Long categoryId, Long supplierId, boolean lowStock) {
        return repository.findAll().stream()
                .filter(p -> categoryId == null
                        || (p.getCategory() != null && p.getCategory().getId().equals(categoryId)))
                .filter(p -> supplierId == null
                        || (p.getSupplier() != null && p.getSupplier().getId().equals(supplierId)))
                .filter(p -> !lowStock || p.getStockQuantity() <= LOW_STOCK_THRESHOLD)
                .map(this::toResponse)
                .toList();
    }

    public ProductResponse findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    public ProductResponse create(ProductRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new NotFoundException("Category not found: " + request.categoryId()));
        Supplier supplier = supplierRepository.findById(request.supplierId())
                .orElseThrow(() -> new NotFoundException("Supplier not found: " + request.supplierId()));

        Product product = Product.builder()
                .title(request.title())
                .category(category)
                .supplier(supplier)
                .price(request.price())
                .stockQuantity(request.stockQuantity())
                .lastRestockedAt(request.lastRestockedAt() != null
                        ? request.lastRestockedAt() : LocalDate.now())
                .build();

        return toResponse(repository.save(product));
    }

    public ProductResponse update(Long id, ProductRequest request) {
        Product product = getOrThrow(id);
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new NotFoundException("Category not found: " + request.categoryId()));
        Supplier supplier = supplierRepository.findById(request.supplierId())
                .orElseThrow(() -> new NotFoundException("Supplier not found: " + request.supplierId()));

        product.setTitle(request.title());
        product.setCategory(category);
        product.setSupplier(supplier);
        product.setPrice(request.price());
        product.setStockQuantity(request.stockQuantity());
        if (request.lastRestockedAt() != null) {
            product.setLastRestockedAt(request.lastRestockedAt());
        }
        return toResponse(repository.update(product));
    }

    public void delete(Long id) {
        getOrThrow(id);
        repository.delete(id);
    }

    public Product getOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found: " + id));
    }

    private ProductResponse toResponse(Product p) {
        return new ProductResponse(
                p.getId(), p.getTitle(), p.getCategory(), p.getSupplier(),
                p.getPrice(), p.getStockQuantity(), p.getLastRestockedAt());
    }
}