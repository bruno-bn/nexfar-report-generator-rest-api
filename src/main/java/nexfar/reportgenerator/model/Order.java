package nexfar.reportgenerator.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Document(collection = "order")
public class Order {

    @Id
    private String id;
    private String status;
    private Client client;
    private List<OrderItem> items;
    private BigDecimal netTotal;
    private BigDecimal totalWithTaxes;
    private LocalDateTime createdAt;
}
