package com.telran.shopmongodb.data;

import com.telran.shopmongodb.data.entity.UserRoleEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRoleRepository extends CrudRepository<UserRoleEntity, String> {
}
