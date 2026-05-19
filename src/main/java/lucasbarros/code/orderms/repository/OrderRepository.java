package lucasbarros.code.orderms.repository;

import lucasbarros.code.orderms.controller.dto.OrderResponse;
import lucasbarros.code.orderms.order.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<OrderEntity, Long> {

    Page<OrderEntity> findAllByCustomerId(Long customeId, PageRequest pageRequest);
}
