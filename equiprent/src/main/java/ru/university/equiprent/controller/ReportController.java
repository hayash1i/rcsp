package ru.university.equiprent.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import ru.university.equiprent.api.ReportApi;
import ru.university.equiprent.dto.RevenueReportDto;
import ru.university.equiprent.service.ReportService;

@RestController
public class ReportController implements ReportApi {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @Override
    public ResponseEntity<byte[]> revenueReport(LocalDate from, LocalDate to, String format) {
        if (from == null) from = LocalDate.now().minusMonths(1);
        if (to == null)   to = LocalDate.now();

        RevenueReportDto report = reportService.buildRevenueReport(from, to);

        if ("xlsx".equalsIgnoreCase(format)) {
            byte[] bytes = toExcel(report);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=revenue_" + from + "_" + to + ".xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(bytes);
        }
        throw new IllegalArgumentException("Unsupported format: " + format);
    }

    private byte[] toExcel(RevenueReportDto report) {
        try (Workbook wb = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet summary = wb.createSheet("Summary");
            Row r0 = summary.createRow(0);
            r0.createCell(0).setCellValue("From");
            r0.createCell(1).setCellValue(report.from().toString());

            Row r1 = summary.createRow(1);
            r1.createCell(0).setCellValue("To");
            r1.createCell(1).setCellValue(report.to().toString());

            Row r2 = summary.createRow(2);
            r2.createCell(0).setCellValue("Total revenue");
            r2.createCell(1).setCellValue(report.totalRevenue().doubleValue());

            Row r3 = summary.createRow(3);
            r3.createCell(0).setCellValue("Total sales");
            r3.createCell(1).setCellValue(report.totalSales());

            Sheet byCat = wb.createSheet("By category");
            Row header = byCat.createRow(0);
            header.createCell(0).setCellValue("Category");
            header.createCell(1).setCellValue("Revenue");
            int i = 1;
            for (Map.Entry<String, BigDecimal> e : report.revenueByCategory().entrySet()) {
                Row row = byCat.createRow(i++);
                row.createCell(0).setCellValue(e.getKey());
                row.createCell(1).setCellValue(e.getValue().doubleValue());
            }

            Sheet byDay = wb.createSheet("By day");
            Row dh = byDay.createRow(0);
            dh.createCell(0).setCellValue("Date");
            dh.createCell(1).setCellValue("Revenue");
            int j = 1;
            for (Map.Entry<LocalDate, BigDecimal> e : report.revenueByDay().entrySet()) {
                Row row = byDay.createRow(j++);
                row.createCell(0).setCellValue(e.getKey().toString());
                row.createCell(1).setCellValue(e.getValue().doubleValue());
            }

            wb.write(out);
            return out.toByteArray();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to build Excel report", ex);
        }
    }
}