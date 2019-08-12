package com.telran.shopmongodb.data.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document(collection = "users")
public class UserEntity {
    @Id
    private String email;
    private String password;
    private List<RoleStatus> roles;
    private UserProfile profile;
}
