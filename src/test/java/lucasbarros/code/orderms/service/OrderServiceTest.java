package lucasbarros.code.orderms.service;

import lucasbarros.code.orderms.controller.dto.OrderResponse;
import lucasbarros.code.orderms.dto.OrderCreatedEvent;
import lucasbarros.code.orderms.dto.OrderItemEvent;
import lucasbarros.code.orderms.order.OrderEntity;
import lucasbarros.code.orderms.repository.OrderRepository;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock OrderRepository repository;
    @Mock MongoTemplate mongoTemplate;
    @InjectMocks OrderService service;

    @Test
    void savesMappedOrderWithCalculatedTotal() {
        var event = new OrderCreatedEvent(1001L, 1L, List.of(
                new OrderItemEvent("lápis", 100, new BigDecimal("1.10")),
                new OrderItemEvent("caderno", 10, new BigDecimal("1.00"))));

        service.save(event);

        var captor = ArgumentCaptor.forClass(OrderEntity.class);
        verify(repository).save(captor.capture());
        var entity = captor.getValue();
        assertThat(entity.getOrderId()).isEqualTo(1001L);
        assertThat(entity.getCustomerId()).isEqualTo(1L);
        assertThat(entity.getTotal()).isEqualByComparingTo("120.00");
        assertThat(entity.getItems()).hasSize(2);
        assertThat(entity.getItems().get(0).getProduct()).isEqualTo("lápis");
    }

    @Test
    void findsOrdersAndMapsEntitiesToResponses() {
        var entity = new OrderEntity();
        entity.setOrderId(1001L);
        entity.setCustomerId(1L);
        entity.setTotal(new BigDecimal("120.00"));
        when(repository.findAllByCustomerId(1L, PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(entity), PageRequest.of(0, 10), 1));

        var result = service.findAllByCustomerId(1L, PageRequest.of(0, 10));

        assertThat(result.getContent()).singleElement().satisfies(response -> {
            assertThat(response.orderId()).isEqualTo(1001L);
            assertThat(response.total()).isEqualByComparingTo("120.00");
        });
    }

    @Test
    void sumsCustomerOrders() {
        var aggregationResults = new AggregationResults<>(List.of(new Document("total", "120.00")),
                new Document("total", "120.00"));
        when(mongoTemplate.aggregate(any(Aggregation.class), eq("tb_orders"), eq(Document.class)))
                .thenReturn(aggregationResults);

        assertThat(service.findTotalOnOrdersByCustomerId(1L)).isEqualByComparingTo("120.00");
        verify(mongoTemplate).aggregate(any(Aggregation.class), eq("tb_orders"), eq(Document.class));
    }
}
