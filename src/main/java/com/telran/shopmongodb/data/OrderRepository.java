package com.telran.shopmongodb.data;

import com.telran.shopmongodb.data.entity.OrderEntity;
import com.telran.shopmongodb.data.projection.UserStatistics;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.stream.Stream;

public interface OrderRepository extends MongoRepository<OrderEntity, String> {
    List<OrderEntity> findByOwner_Email(String userEmail);

    Stream<UserStatistics> getMostActiveUser();

    Stream<UserStatistics> getMostProfitableUser();
}
