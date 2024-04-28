package nexfar.reportgenerator.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItem {

    private Product product;
    private Integer quantity;
    private Double price;
    private Double finalPrice;
}
