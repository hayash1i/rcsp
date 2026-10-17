package ru.university.equiprent.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SaleItem {
    private Long id;
    private Product product;
    private int quantity;
    private int priceAtSaleTime;
}