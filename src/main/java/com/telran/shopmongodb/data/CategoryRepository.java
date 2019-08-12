package com.telran.shopmongodb.data;

import com.telran.shopmongodb.data.entity.CategoryEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.stream.Stream;

public interface CategoryRepository extends MongoRepository<CategoryEntity, String> {
    Stream<CategoryEntity> findBy();
}
