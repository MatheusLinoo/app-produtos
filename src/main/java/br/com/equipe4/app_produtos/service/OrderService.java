package br.com.equipe4.app_produtos.service;

import br.com.equipe4.app_produtos.infra.exceptions.EmptyOrderException;
import br.com.equipe4.app_produtos.infra.exceptions.EntityNotFoundException;
import br.com.equipe4.app_produtos.mapper.OrderMapper;
import br.com.equipe4.app_produtos.model.Order;
import br.com.equipe4.app_produtos.model.OrderItem;
import br.com.equipe4.app_produtos.model.TransactionType;
import br.com.equipe4.app_produtos.repository.OrderRepository;
import br.com.equipe4.app_produtos.service.dto.StockMovementDTO;
import br.com.equipe4.app_produtos.service.dto.request.OrderRequestDTO;
import br.com.equipe4.app_produtos.service.dto.response.OrderResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper mapper;
    private final InventoryService inventoryService;

    @Transactional
    public OrderResponseDTO create(OrderRequestDTO orderRequestDTO) {

        Order order = mapper.toEntity(orderRequestDTO);

        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new EmptyOrderException("The order must contain items.");
        }

        for (OrderItem item : order.getItems()) {
            item.setOrder(order);
        }

        BigDecimal total = order.getItems()
                .stream()
                .map(item -> item.getPriceSnapshot().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal freight = order.getFreight() != null ? order.getFreight() : BigDecimal.ZERO;
        BigDecimal discount = order.getDiscount() != null ? order.getDiscount() : BigDecimal.ZERO;

        order.setTotal(total.add(freight).subtract(discount));
        order.setStatus("CREATED");

        Order savedOrder = orderRepository.save(order);

        for (OrderItem item : savedOrder.getItems()) {
            StockMovementDTO movement = new StockMovementDTO(
                TransactionType.EXIT, 
                item.getQuantity(),
                "Venda Pedido #" + savedOrder.getId()
            );
            
            inventoryService.processTransaction(
                item.getProductId(), 
                movement, 
                null 
            );
        }

        return mapper.toDTO(savedOrder);
    }



    public OrderResponseDTO findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found."));
        return mapper.toDTO(order);
    }
}
