package br.com.equipe4.app_produtos.repository;

import br.com.equipe4.app_produtos.model.Inventory;
import br.com.equipe4.app_produtos.service.dto.reports.LowStockProductDTO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    @Query("""
                SELECT new br.com.equipe4.app_produtos.service.dto.reports.LowStockProductDTO(
                    inv.product.id,
                    inv.product.name,
                    inv.quantity,
                    inv.minLevel
                )
                FROM Inventory inv
                WHERE inv.quantity <= inv.minLevel
            """)
    List<LowStockProductDTO> findLowStockProducts();
}
