package lucasbarros.code.orderms.controller;

import lucasbarros.code.orderms.controller.dto.ApiResponse;
import lucasbarros.code.orderms.controller.dto.OrderResponse;
import lucasbarros.code.orderms.controller.dto.PaginationResponse;
import lucasbarros.code.orderms.service.OrderService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class OrderController {

    private final OrderService orderService;


    public OrderController(OrderService orderService) {
        this.orderService = orderService;
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
}
