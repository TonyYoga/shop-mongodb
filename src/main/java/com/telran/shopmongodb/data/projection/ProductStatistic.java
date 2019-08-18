package com.telran.shopmongodb.data.projection;

import lombok.*;

import java.math.BigDecimal;
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ProductStatistic {
    private String productName;
    private String productCategory;
    private long numberOfPurchases;
    private BigDecimal totalAmount;
}
