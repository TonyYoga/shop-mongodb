package com.telran.shopmongodb.controller;

import com.telran.shopmongodb.controller.dto.AuthDto;
import com.telran.shopmongodb.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @Autowired
    AuthService service;
    @PostMapping("registration")
    public void registration(@RequestBody AuthDto authDto) {
        service.registration(authDto.getEmail(), authDto.getPassword());
    }
}
