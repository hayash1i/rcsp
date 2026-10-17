package ru.university.equiprent.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record SaleRequest(
        @NotNull(message = "storeId is required") Long storeId,
        @NotNull(message = "cashierId is required") Long cashierId,
        @NotEmpty(message = "items cannot be empty") @Valid List<SaleItemRequest> items
) {}