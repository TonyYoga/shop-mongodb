package com.telran.shopmongodb.data;

import com.telran.telranshopspringdata.data.entity.CategoryEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.stream.Stream;

public interface CategoryRepository extends CrudRepository<CategoryEntity,String> {
    Stream<CategoryEntity> findBy();
}
