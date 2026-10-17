package ru.university.equiprent.api;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ru.university.equiprent.dto.SaleRequest;
import ru.university.equiprent.dto.SaleResponse;

@RequestMapping("/api/sales")
@Tag(name = "Sale Controller")
public interface SaleApi {

    @PostMapping
    SaleResponse register(@RequestBody @Valid SaleRequest request);

    @GetMapping
    List<SaleResponse> getAll(
            @RequestParam(required = false) Long storeId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to);

    @GetMapping("/{id}")
    SaleResponse get(@PathVariable Long id);
}