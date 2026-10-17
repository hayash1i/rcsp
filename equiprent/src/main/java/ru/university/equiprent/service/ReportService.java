package ru.university.equiprent.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.university.equiprent.dto.RevenueReportDto;
import ru.university.equiprent.model.Sale;
import ru.university.equiprent.model.SaleItem;
import ru.university.equiprent.repository.SaleRepository;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final SaleRepository saleRepository;

    public RevenueReportDto buildRevenueReport(LocalDate from, LocalDate to) {
        LocalDateTime fromDt = from.atStartOfDay();
        LocalDateTime toDt = to.plusDays(1).atStartOfDay();

        BigDecimal total = BigDecimal.ZERO;
        int salesCount = 0;
        Map<String, BigDecimal> byCategory = new LinkedHashMap<>();
        Map<LocalDate, BigDecimal> byDay = new LinkedHashMap<>();

        for (Sale sale : saleRepository.findAll()) {
            if (sale.getSoldAt().isBefore(fromDt) || !sale.getSoldAt().isBefore(toDt)) continue;
            salesCount++;
            LocalDate day = sale.getSoldAt().toLocalDate();
            for (SaleItem item : sale.getItems()) {
                BigDecimal line = BigDecimal.valueOf(
                        (long) item.getQuantity() * item.getPriceAtSaleTime());
                total = total.add(line);
                String cat = item.getProduct().getCategory() != null
                        ? item.getProduct().getCategory().getName() : "Без категории";
                byCategory.merge(cat, line, BigDecimal::add);
                byDay.merge(day, line, BigDecimal::add);
            }
        }
        return new RevenueReportDto(from, to, total, salesCount, byCategory, byDay);
    }
}