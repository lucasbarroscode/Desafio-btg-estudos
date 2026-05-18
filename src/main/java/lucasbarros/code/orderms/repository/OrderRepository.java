package lucasbarros.code.orderms.repository;

import lucasbarros.code.orderms.order.OrderEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<OrderEntity, Long> {
}
