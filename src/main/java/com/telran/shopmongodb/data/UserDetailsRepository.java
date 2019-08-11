package com.telran.shopmongodb.data;

import com.telran.shopmongodb.data.entity.UserDetailsEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserDetailsRepository extends CrudRepository<UserDetailsEntity, String> {
}
