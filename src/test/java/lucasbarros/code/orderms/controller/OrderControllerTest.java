package lucasbarros.code.orderms.controller;

import lucasbarros.code.orderms.dto.OrderCreatedEvent;
import lucasbarros.code.orderms.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;

import static lucasbarros.code.orderms.config.RabbitMqConfig.ORDER_CREATE_EXCHANGE;
import static lucasbarros.code.orderms.config.RabbitMqConfig.ORDER_CREATE_ROUTING_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {
    @Mock OrderService orderService;
    @Mock RabbitTemplate rabbitTemplate;
    @InjectMocks OrderController controller;

    @Test
    void listsCustomerOrdersWithSummaryAndPagination() {
        when(orderService.findAllByCustomerId(1L, PageRequest.of(1, 5)))
                .thenReturn(new PageImpl<>(List.of(), PageRequest.of(1, 5), 5));
        when(orderService.findTotalOnOrdersByCustomerId(1L)).thenReturn(new BigDecimal("120.00"));

        var response = controller.listOrders(1L, 1, 5);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody().summary()).containsEntry("totalOnOrders", new BigDecimal("120.00"));
        assertThat(response.getBody().pagination().page()).isEqualTo(1);
        assertThat(response.getBody().pagination().pageSize()).isEqualTo(5);
    }

    @Test
    void publishesOrderAndReturnsAccepted() {
        var event = new OrderCreatedEvent(1L, 2L, List.of());

        var response = controller.publishOrder(event);

        verify(rabbitTemplate).convertAndSend(ORDER_CREATE_EXCHANGE, ORDER_CREATE_ROUTING_KEY, event);
        assertThat(response.getStatusCode().value()).isEqualTo(202);
    }
}
