package com.telran.shopmongodb.data.projection;

import com.telran.shopmongodb.data.entity.ProductOrder;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class UserStatistic {
    private String userEmail;
    private long totalProductsCount;
    private BigDecimal totalAmount;
    private List<ProductOrder> productList;

}
