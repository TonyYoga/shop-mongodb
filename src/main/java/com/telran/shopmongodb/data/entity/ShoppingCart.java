package com.telran.shopmongodb.data.entity;

import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ShoppingCart {
    private Timestamp date;
    private List<ProductOrder> products;
    private UserProfile owner;
}
