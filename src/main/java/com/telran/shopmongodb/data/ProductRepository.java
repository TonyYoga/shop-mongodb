package com.telran.shopmongodb.data;

import com.telran.shopmongodb.data.entity.ProductEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.stream.Stream;

public interface ProductRepository extends CrudRepository<ProductEntity,String> {
    Stream<ProductEntity> findAllByCategory_Id(String category);
    Stream<ProductEntity> findBy();
}
