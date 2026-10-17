package ru.university.equiprent.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Product {
    private Long id;
    private String title;
    private Category category;
    private Supplier supplier;
    private int price;
    private int stockQuantity;
    private LocalDate lastRestockedAt;
}