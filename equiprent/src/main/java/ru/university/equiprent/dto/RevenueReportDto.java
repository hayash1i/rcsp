package ru.university.equiprent.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public record RevenueReportDto(
        LocalDate from,
        LocalDate to,
        BigDecimal totalRevenue,
        int totalSales,
        Map<String, BigDecimal> revenueByCategory,
        Map<LocalDate, BigDecimal> revenueByDay
) {}