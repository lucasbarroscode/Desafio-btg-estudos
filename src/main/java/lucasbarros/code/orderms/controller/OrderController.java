package lucasbarros.code.orderms.controller;

import lucasbarros.code.orderms.controller.dto.ApiResponse;
import lucasbarros.code.orderms.controller.dto.OrderResponse;
import lucasbarros.code.orderms.controller.dto.PaginationResponse;
import lucasbarros.code.orderms.dto.OrderCreatedEvent;
import lucasbarros.code.orderms.listener.OrderCreatedListener;
import lucasbarros.code.orderms.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static lucasbarros.code.orderms.config.RabbitMqConfig.ORDER_CREATE_EXCHANGE;
import static lucasbarros.code.orderms.config.RabbitMqConfig.ORDER_CREATE_ROUTING_KEY;

@RestController
public class OrderController {

    private final OrderService orderService;
    private final RabbitTemplate rabbitTemplate;
    private final Logger logger = LoggerFactory.getLogger(OrderController.class);


    public OrderController(OrderService orderService, RabbitTemplate rabbitTemplate) {
        this.orderService = orderService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/customers/{customerId}/orders")
    public ResponseEntity<ApiResponse<OrderResponse>> listOrders(@PathVariable("customerId")Long customerId,
                                                                 @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){

    var pageResponse = orderService.findAllByCustomerId(customerId, PageRequest.of(page, pageSize));
    var totalOnOrders = orderService.findTotalOnOrdersByCustomerId(customerId);

    return ResponseEntity.ok(new ApiResponse<>(
            Map.of("totalOnOrders", totalOnOrders),
            pageResponse.getContent(),
            PaginationResponse.fromPage(pageResponse)
    ));

    }

    @PostMapping("/publish")
    public ResponseEntity<Void> publishOrder(@RequestBody OrderCreatedEvent body) {

        logger.info("Publishing order to RabbitMQ: {}", body);

        rabbitTemplate.convertAndSend(
                ORDER_CREATE_EXCHANGE,
                ORDER_CREATE_ROUTING_KEY,
                body
        );

        return ResponseEntity.accepted().build();
    }
}
