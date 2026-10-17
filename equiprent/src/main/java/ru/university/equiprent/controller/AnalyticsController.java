package ru.university.equiprent.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import ru.university.equiprent.api.AnalyticsApi;
import ru.university.equiprent.dto.AbcAnalysisDto;
import ru.university.equiprent.dto.TopProductDto;
import ru.university.equiprent.service.AnalyticsService;

@RestController
public class AnalyticsController implements AnalyticsApi {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @Override
    public List<TopProductDto> topProducts(String period, int limit) {
        return analyticsService.topProducts(period, limit);
    }

    @Override
    public List<AbcAnalysisDto> abc() {
        return analyticsService.abcAnalysis();
    }
}