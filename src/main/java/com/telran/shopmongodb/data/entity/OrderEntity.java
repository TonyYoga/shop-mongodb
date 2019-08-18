package com.telran.shopmongodb.data.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

//import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "orders")
public class OrderEntity {
    @Id
    private String id;
    private LocalDateTime date;
    private OrderStatus status;
    @Indexed()
    private String ownerEmail;
    private List<ProductOrder> products;
}
