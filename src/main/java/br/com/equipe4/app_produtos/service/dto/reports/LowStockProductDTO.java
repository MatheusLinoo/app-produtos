package br.com.equipe4.app_produtos.service.dto.reports;

public record LowStockProductDTO(
        Long productId,
        String productName,
        Integer currentQuantity,
        Integer minLevel) {

}
