package com.telran.shopmongodb.controller.dto;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ProductDto {
    private String id;
    private String name;
    private BigDecimal price;
    private CategoryDto category;
}
