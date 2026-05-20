package lucasbarros.code.orderms.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String ORDER_CREATE_QUEUE = "btg-pactual-order-created";
    public static final String ORDER_CREATE_EXCHANGE = "btg-pactual-order-exchange";
    public static final String ORDER_CREATE_ROUTING_KEY = "btg-pactual-order-created-routing-key";

    @Bean
    public JacksonJsonMessageConverter  jackson2JsonMessageConverter() {
        return new JacksonJsonMessageConverter ();
    }

    @Bean
    public Declarable orderCreatedQueu(){
        return new Queue(ORDER_CREATE_QUEUE);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(ORDER_CREATE_EXCHANGE);
    }

    @Bean
    public Binding orderCreatedBinding() {
        return BindingBuilder
                .bind(orderCreatedQueue())
                .to(exchange())
                .with(ORDER_CREATE_ROUTING_KEY);
    }

    @Bean
    public Queue orderCreatedQueue() {
        return new Queue(ORDER_CREATE_QUEUE);
    }




}
