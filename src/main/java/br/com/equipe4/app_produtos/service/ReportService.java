package br.com.equipe4.app_produtos.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import br.com.equipe4.app_produtos.repository.InventoryRepository;
import br.com.equipe4.app_produtos.repository.OrderItemRepository;
import br.com.equipe4.app_produtos.repository.OrderRepository;
import br.com.equipe4.app_produtos.service.dto.reports.LowStockProductDTO;
import br.com.equipe4.app_produtos.service.dto.reports.SalesMetricsDTO;
import br.com.equipe4.app_produtos.service.dto.reports.TopProductDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final InventoryRepository inventoryRepository;

    public SalesMetricsDTO getSalesMetrics(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);
        return orderRepository.getSalesMetrics(start, end);
    }

    public List<TopProductDTO> getTopSellingProducts(int limit) {
        return orderItemRepository.findTopSellingProducts(PageRequest.of(0, limit));
    }

    public List<LowStockProductDTO> getLowStockProducts() {
        return inventoryRepository.findLowStockProducts();
    }

}
