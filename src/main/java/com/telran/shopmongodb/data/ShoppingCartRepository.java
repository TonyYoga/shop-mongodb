package com.telran.shopmongodb.data;

import com.telran.shopmongodb.data.entity.ShoppingCartEntity;
import org.springframework.data.repository.CrudRepository;

public interface ShoppingCartRepository extends CrudRepository<ShoppingCartEntity, String> {
    ShoppingCartEntity findShoppingCartEntityByOwner_Email(String email);
}
