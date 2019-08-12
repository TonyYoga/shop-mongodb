package com.telran.shopmongodb.service;

import com.telran.shopmongodb.data.UserRepository;
import com.telran.shopmongodb.data.entity.RoleStatus;
import com.telran.shopmongodb.data.entity.UserEntity;
import com.telran.shopmongodb.service.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    UserRepository repository;
    @Autowired
    PasswordEncoder encoder;
    @Override
    public void registration(String email, String password) {
        if (repository.existsById(email)) {
            throw new ServiceException("User already exist!");
        }
        UserEntity entity = UserEntity.builder()
                .email(email)
                .password(encoder.encode(password))
                .roles(List.of(RoleStatus.ROLE_USER)
                ).build();
        repository.save(entity);
    }
}
