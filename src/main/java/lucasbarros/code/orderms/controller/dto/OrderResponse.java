package lucasbarros.code.orderms.controller.dto;

import lucasbarros.code.orderms.order.OrderEntity;

import java.math.BigDecimal;

public record OrderResponse(Long orderId,
                            Long customeId,
                            BigDecimal total) {
    public static OrderResponse fromEntity(OrderEntity entity){
        return new OrderResponse(entity.getOrderId(), entity.getCustomerId(), entity.getTotal());
    }
}
