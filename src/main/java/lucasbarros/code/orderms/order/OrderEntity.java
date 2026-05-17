package lucasbarros.code.orderms.order;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;
import java.util.List;

//por conta do mongoDb como se fosse o nome da tabel
@Document(collection = "tb_orders")
public class OrderEntity {

    @MongoId
    private Long orderId;

    //PARA TER UMA PERFORMANCE MELHOR VAMOS CRIAR UM INDICE EM CIMA DESSE ID
    @Indexed(name = "customer_id_index")
    private Long customerId;

    //NO MONGO ELE SALVA BIGDECIMA COMO STRING, AI É INTERESSANTE SALVARMOS COMO BIGDECIMAL MESMO
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal total;

    private List<OrderItem> items;

    public OrderEntity() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}
