package lucasbarros.code.orderms.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RabbitMqConfigTest {
    private final RabbitMqConfig config = new RabbitMqConfig();

    @Test
    void createsRabbitComponentsWithExpectedNames() {
        assertThat(config.jackson2JsonMessageConverter()).isNotNull();
        assertThat(config.orderCreatedQueue().getName()).isEqualTo(RabbitMqConfig.ORDER_CREATE_QUEUE);
        assertThat(config.orderCreatedQueu()).isInstanceOf(org.springframework.amqp.core.Queue.class);
        assertThat(config.exchange().getName()).isEqualTo(RabbitMqConfig.ORDER_CREATE_EXCHANGE);
        assertThat(config.orderCreatedBinding().getRoutingKey())
                .isEqualTo(RabbitMqConfig.ORDER_CREATE_ROUTING_KEY);
    }
}
