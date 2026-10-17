package ru.university.equiprent.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Sale {
    private Long id;
    private Store store;
    private List<SaleItem> items;
    private LocalDateTime soldAt;
    private User cashier;
}