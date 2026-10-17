package ru.university.equiprent.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductRequest(
        @NotBlank(message = "Title cannot be blank") String title,
        @NotNull(message = "categoryId is required") Long categoryId,
        @NotNull(message = "supplierId is required") Long supplierId,
        @Min(value = 0, message = "price must be >= 0") int price,
        @Min(value = 0, message = "stockQuantity must be >= 0") int stockQuantity,
        LocalDate lastRestockedAt
) {}