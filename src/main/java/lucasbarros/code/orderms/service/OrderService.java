package lucasbarros.code.orderms.service;

import lucasbarros.code.orderms.controller.dto.OrderResponse;
import lucasbarros.code.orderms.dto.OrderCreatedEvent;
import lucasbarros.code.orderms.order.OrderEntity;
import lucasbarros.code.orderms.order.OrderItem;
import lucasbarros.code.orderms.repository.OrderRepository;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final MongoTemplate mongoTemplate;

    public OrderService(OrderRepository repository, MongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
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

    public BigDecimal findTotalOnOrdersByCustomerId (Long customId){
        var aggregations = newAggregation(
                match(Criteria.where("customerId").is(customId)),
                group().sum("total").as("total")
        );

        var response = mongoTemplate.aggregate(aggregations, "tb_orders", Document.class);

        return new BigDecimal(response.getUniqueMappedResult().get("total").toString()) ;
    }
}
