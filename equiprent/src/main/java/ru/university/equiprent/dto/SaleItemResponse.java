package ru.university.equiprent.dto;

public record SaleItemResponse(
        Long id,
        Long productId,
        String productTitle,
        int quantity,
        int priceAtSaleTime,
        int lineTotal
) {}