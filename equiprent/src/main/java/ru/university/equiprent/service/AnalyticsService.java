package ru.university.equiprent.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.university.equiprent.dto.AbcAnalysisDto;
import ru.university.equiprent.dto.TopProductDto;
import ru.university.equiprent.model.Sale;
import ru.university.equiprent.model.SaleItem;
import ru.university.equiprent.repository.ProductRepository;
import ru.university.equiprent.repository.SaleRepository;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;

    /** Топ-N товаров по выручке за период. period: "day", "week", "month", "year", "all". */
    public List<TopProductDto> topProducts(String period, int limit) {
        if (limit <= 0) {
            return List.of();
        }
        LocalDateTime from = resolveFrom(period);
        Map<Long, TopProductDto> agg = new LinkedHashMap<>();
        for (Sale sale : saleRepository.findAll()) {
            if (from != null && sale.getSoldAt().isBefore(from)) continue;
            for (SaleItem item : sale.getItems()) {
                Long pid = item.getProduct().getId();
                BigDecimal line = BigDecimal.valueOf(
                        (long) item.getQuantity() * item.getPriceAtSaleTime());
                TopProductDto existing = agg.get(pid);
                if (existing == null) {
                    agg.put(pid, new TopProductDto(pid, item.getProduct().getTitle(),
                            item.getQuantity(), line));
                } else {
                    agg.put(pid, new TopProductDto(pid, existing.productTitle(),
                            existing.totalQuantitySold() + item.getQuantity(),
                            existing.totalRevenue().add(line)));
                }
            }
        }
        return agg.values().stream()
                .sorted(Comparator.comparing(TopProductDto::totalRevenue).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    /** ABC-анализ: сортировка по выручке, класс A — 80%, B — 95%, C — остальное. */
    public List<AbcAnalysisDto> abcAnalysis() {
        Map<Long, BigDecimal> revenueByProduct = new LinkedHashMap<>();
        Map<Long, String> titles = new LinkedHashMap<>();
        for (Sale sale : saleRepository.findAll()) {
            for (SaleItem item : sale.getItems()) {
                Long pid = item.getProduct().getId();
                BigDecimal line = BigDecimal.valueOf(
                        (long) item.getQuantity() * item.getPriceAtSaleTime());
                revenueByProduct.merge(pid, line, BigDecimal::add);
                titles.put(pid, item.getProduct().getTitle());
            }
        }
        BigDecimal total = revenueByProduct.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (total.compareTo(BigDecimal.ZERO) == 0) {
            return List.of();
        }

        List<Map.Entry<Long, BigDecimal>> sorted = new ArrayList<>(revenueByProduct.entrySet());
        sorted.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        List<AbcAnalysisDto> result = new ArrayList<>();
        double cumulative = 0.0;
        for (Map.Entry<Long, BigDecimal> e : sorted) {
            double share = e.getValue().doubleValue() / total.doubleValue();
            cumulative += share;
            String abc;
            if (cumulative <= 0.80) {
                abc = "A";
            } else if (cumulative <= 0.95) {
                abc = "B";
            } else {
                abc = "C";
            }
            result.add(new AbcAnalysisDto(
                    e.getKey(),
                    titles.get(e.getKey()),
                    e.getValue().setScale(2, RoundingMode.HALF_UP),
                    round4(share),
                    round4(cumulative),
                    abc));
        }
        return result;
    }

    private LocalDateTime resolveFrom(String period) {
        if (period == null || period.isBlank() || "all".equalsIgnoreCase(period)) {
            return null;
        }
        LocalDateTime now = LocalDateTime.now();
        return switch (period.toLowerCase()) {
            case "day"   -> now.minusDays(1);
            case "week"  -> now.minusWeeks(1);
            case "month" -> now.minusMonths(1);
            case "year"  -> now.minusYears(1);
            default -> throw new IllegalArgumentException("Unknown period: " + period);
        };
    }

    private static double round4(double v) {
        return BigDecimal.valueOf(v).setScale(4, RoundingMode.HALF_UP).doubleValue();
    }
}