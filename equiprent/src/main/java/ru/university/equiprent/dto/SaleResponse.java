package ru.university.equiprent.dto;

import java.time.LocalDateTime;
import java.util.List;

public record SaleResponse(
        Long id,
        Long storeId,
        String storeAddress,
        Long cashierId,
        String cashierName,
        LocalDateTime soldAt,
        List<SaleItemResponse> items,
        int totalAmount
) {}