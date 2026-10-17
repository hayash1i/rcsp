package ru.university.equiprent.dto;

import java.math.BigDecimal;

public record TopProductDto(
        Long productId,
        String productTitle,
        int totalQuantitySold,
        BigDecimal totalRevenue
) {}