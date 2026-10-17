package ru.university.equiprent.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ru.university.equiprent.dto.ProductRequest;
import ru.university.equiprent.dto.ProductResponse;

@RequestMapping("/api/products")
@Tag(name = "Product Controller")
public interface ProductApi {

    @GetMapping
    List<ProductResponse> getAll(
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) Long supplier,
            @RequestParam(required = false, defaultValue = "false") boolean lowStock);

    @GetMapping("/{id}")
    ProductResponse get(@PathVariable Long id);

    @PostMapping
    ProductResponse create(@RequestBody @Valid ProductRequest request);

    @PutMapping("/{id}")
    ProductResponse update(@PathVariable Long id, @RequestBody @Valid ProductRequest request);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);
}