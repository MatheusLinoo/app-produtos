package br.com.equipe4.app_produtos.service.dto.reports;

import java.math.BigDecimal;

public record SalesMetricsDTO(
        BigDecimal totalRevenue,
        Long totalOrders) {

}
