package lucasbarros.code.orderms.service;

import lucasbarros.code.orderms.controller.dto.OrderResponse;
import lucasbarros.code.orderms.dto.OrderCreatedEvent;
import lucasbarros.code.orderms.order.OrderEntity;
import lucasbarros.code.orderms.order.OrderItem;
import lucasbarros.code.orderms.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository repository;

    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    public void save(OrderCreatedEvent event){

        var entity = new OrderEntity();
        entity.setOrderId(event.codigoPedido());
        entity.setCustomerId(event.codigoCliente());
        entity.setItems(getOrderItens(event));
        entity.setTotal(getTotal(event));

        repository.save(entity);

    }

    public Page<OrderResponse> findAllByCustomerId(Long customeId, PageRequest pageRequest){
        var orders = repository.findAllByCustomerId(customeId, pageRequest);
        //todo verificar se eu não poderia fazer igual o map ali embaixo e qual seria a diferença
        return orders.map(OrderResponse::fromEntity);
    }

    private BigDecimal getTotal(OrderCreatedEvent event) {
        //reduce acumula o total do pedido para mostrar apenas o valor total
        return event.itens().stream()
                .map(i -> i.preco().multiply(BigDecimal.valueOf(i.quantidade())))
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    private static List<OrderItem> getOrderItens(OrderCreatedEvent event) {
        return event.itens().stream().
                map(i -> new OrderItem(i.produto(), i.quantidade(), i.preco()))
                .toList();
    }
}
