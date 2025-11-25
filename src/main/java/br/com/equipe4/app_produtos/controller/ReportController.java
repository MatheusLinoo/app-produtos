package br.com.equipe4.app_produtos.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.equipe4.app_produtos.service.ReportService;
import br.com.equipe4.app_produtos.service.dto.reports.LowStockProductDTO;
import br.com.equipe4.app_produtos.service.dto.reports.SalesMetricsDTO;
import br.com.equipe4.app_produtos.service.dto.reports.TopProductDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/sales")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public ResponseEntity<SalesMetricsDTO> getSales(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        
        if (start == null) start = LocalDate.now().withDayOfMonth(1);
        if (end == null) end = LocalDate.now();

        return ResponseEntity.ok(reportService.getSalesMetrics(start, end));
    }

    @GetMapping("/top-products")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public ResponseEntity<List<TopProductDTO>> getTopProducts(@RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(reportService.getTopSellingProducts(limit));
    }

    @GetMapping("/low-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public ResponseEntity<List<LowStockProductDTO>> getLowStock() {
        return ResponseEntity.ok(reportService.getLowStockProducts());
    }
}
