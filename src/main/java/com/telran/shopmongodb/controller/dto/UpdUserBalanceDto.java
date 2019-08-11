package com.telran.shopmongodb.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class UpdUserBalanceDto {
    private String userId;
    private BigDecimal balance;
}
