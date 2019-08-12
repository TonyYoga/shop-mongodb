package com.telran.shopmongodb.data.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "orders")
public class OrderEntity {
    @Id
    private String id;
    private Timestamp date;
    private OrderStatus status;
    private String ownerEmail;
    private List<ProductOrder> products;
}
