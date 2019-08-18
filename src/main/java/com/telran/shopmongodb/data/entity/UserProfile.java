package com.telran.shopmongodb.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserProfile {
    private String name;
    private String phone;
    private BigDecimal balance;
    private List<ProductOrder> shoppingCart;
}
