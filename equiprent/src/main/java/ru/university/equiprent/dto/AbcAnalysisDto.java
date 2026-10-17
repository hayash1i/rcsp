package ru.university.equiprent.dto;

import java.math.BigDecimal;

public record AbcAnalysisDto(
        Long productId,
        String productTitle,
        BigDecimal totalRevenue,
        double revenueShare,
        double cumulativeShare,
        String abcClass
) {}