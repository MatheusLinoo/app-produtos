package br.com.equipe4.app_produtos.repository;

import br.com.equipe4.app_produtos.model.Order;
import br.com.equipe4.app_produtos.service.dto.reports.SalesMetricsDTO;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
                SELECT new br.com.equipe4.app_produtos.service.dto.reports.SalesMetricsDTO(
                    COALESCE(SUM(o.total), 0),
                    COUNT(o)
                )
                FROM Order o
                WHERE o.createdAt BETWEEN :start AND :end
                AND o.status <> 'CANCELLED'
            """)
    SalesMetricsDTO getSalesMetrics(LocalDateTime start, LocalDateTime end);
}
