package com.telran.shopmongodb.data.entity;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductOrder {
    private String productId;
    private String name;
    private int count;
    private BigDecimal price;
    private CategoryEntity category;
}
