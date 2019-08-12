package com.telran.shopmongodb.data;

import com.telran.shopmongodb.data.entity.OrderEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<OrderEntity, String> {
    List<OrderEntity> findByOwnerEmail(String userEmail);

//    Stream<UserStatistics> getMostActiveUser();
//
//    Stream<UserStatistics> getMostProfitableUser();
}
