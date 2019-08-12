package com.telran.shopmongodb.service;

import com.telran.shopmongodb.controller.dto.*;
import com.telran.shopmongodb.data.entity.*;
import com.telran.shopmongodb.data.projection.ProductStatistics;
import com.telran.shopmongodb.data.projection.UserStatistics;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class Mapper {
    public static UserDto map(UserProfile entity) {
        return UserDto.builder()
                .name(entity.getName())
                .phone(entity.getPhone())
                .balance(entity.getBalance())
                .build();
    }

    public static ProductDto map(ProductEntity entity) {
        return ProductDto.builder()
                .category(map(entity.getCategory()))
                .name(entity.getName())
                .id(entity.getId())
                .price(entity.getPrice())
                .build();
    }

    public static CategoryDto map(CategoryEntity entity) {
        return CategoryDto.builder()
                .name(entity.getName())
                .build();
    }

    public static ShoppingCartDto map(List<ProductOrder> shoppingCart) {
        return ShoppingCartDto.builder()
                .products(shoppingCart
                        .stream()
                        .map(Mapper::map)
                        .collect(toList())
                )
                .build();
    }

    public static ProductOrderDto map(ProductOrder entity) {
        return ProductOrderDto.builder()
                .product(ProductDto.builder()
                        .id(entity.getProductId())
                        .name(entity.getName())
                        .price(entity.getPrice())
                        .category(map(entity.getCategory()))
                        .build())
                .count(entity.getCount())
                .build();
    }

    public static OrderDto map(OrderEntity entity) {
        return OrderDto.builder()
                .date(entity.getDate())
                .id(entity.getId())
                .products(entity.getProducts()
                        .stream()
                        .map(Mapper::map)
                        .collect(toList()))
                .status(entity.getStatus().name())
                .build();
    }

    static ProductStatisticDto map(ProductStatistics statistics) {
        return ProductStatisticDto.builder()
                .productName(statistics.getProductName())
                .productCategory(statistics.getProductCategory())
                .numberOfPurchases(statistics.getNumberOfPurchases())
                .totalAmount(statistics.getTotalAmount())
                .build();
    }

    static UserStatisticDto map(UserStatistics statistics) {
        return UserStatisticDto.builder()
                .userEmail(statistics.getUserEmail())
                .totalAmount(statistics.getTotalAmount())
                .totalProductsCount(statistics.getTotalProductsCount())
                .build();
    }
}
