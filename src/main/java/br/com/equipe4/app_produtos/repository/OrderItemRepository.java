package br.com.equipe4.app_produtos.repository;

import br.com.equipe4.app_produtos.model.OrderItem;
import br.com.equipe4.app_produtos.service.dto.reports.TopProductDTO;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("""
                SELECT new br.com.equipe4.app_produtos.service.dto.reports.TopProductDTO(
                    p.name,
                    SUM(i.quantity)
                )
                FROM OrderItem i
                JOIN i.product p
                JOIN i.order o
                WHERE o.status <> 'CANCELLED'
                GROUP BY p.name
                ORDER BY SUM(i.quantity) DESC
            """)
    List<TopProductDTO> findTopSellingProducts(Pageable pageable);
}
