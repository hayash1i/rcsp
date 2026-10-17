package ru.university.equiprent.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.tags.Tag;
import ru.university.equiprent.dto.AbcAnalysisDto;
import ru.university.equiprent.dto.TopProductDto;

@RequestMapping("/api/analytics")
@Tag(name = "Analytics Controller")
public interface AnalyticsApi {

    @GetMapping("/top-products")
    List<TopProductDto> topProducts(
            @RequestParam(defaultValue = "month") String period,
            @RequestParam(defaultValue = "10") int limit);

    @GetMapping("/abc")
    List<AbcAnalysisDto> abc();
}