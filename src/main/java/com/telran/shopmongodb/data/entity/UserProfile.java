package com.telran.shopmongodb.data.entity;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "email")
public class UserProfile {
    private String name;
    private String phone;
    private BigDecimal balance;
    private List<ProductOrder> shoppingCart;
}
