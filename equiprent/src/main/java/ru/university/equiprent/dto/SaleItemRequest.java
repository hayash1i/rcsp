package ru.university.equiprent.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SaleItemRequest(
        @NotNull(message = "productId is required") Long productId,
        @Min(value = 1, message = "quantity must be >= 1") int quantity
) {}