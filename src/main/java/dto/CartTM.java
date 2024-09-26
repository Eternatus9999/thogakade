package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class CartTM {
    private String itemCode;
    private String description;
    private Integer qty;
    private Double unitPrice;
    private Double total;
}