package ru.university.equiprent.dto;

import java.time.LocalDate;

import ru.university.equiprent.model.Category;
import ru.university.equiprent.model.Supplier;

public record ProductResponse(
        Long id,
        String title,
        Category category,
        Supplier supplier,
        int price,
        int stockQuantity,
        LocalDate lastRestockedAt
) {}