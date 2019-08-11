package com.telran.shopmongodb.data;

import com.telran.shopmongodb.data.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity,String> {
}
