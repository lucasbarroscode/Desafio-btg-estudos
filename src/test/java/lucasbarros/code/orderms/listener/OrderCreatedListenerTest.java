package lucasbarros.code.orderms.listener;

import lucasbarros.code.orderms.dto.OrderCreatedEvent;
import lucasbarros.code.orderms.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.support.MessageBuilder;

import java.util.List;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderCreatedListenerTest {
    @Mock OrderService orderService;
    @InjectMocks OrderCreatedListener listener;

    @Test
    void savesMessagePayload() {
        var event = new OrderCreatedEvent(1L, 2L, List.of());

        listener.listen(MessageBuilder.withPayload(event).build());

        verify(orderService).save(event);
    }
}
