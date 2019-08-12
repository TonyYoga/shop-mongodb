package com.telran.shopmongodb.service;


import com.telran.shopmongodb.data.UserRepository;
import com.telran.shopmongodb.data.entity.RoleStatus;
import com.telran.shopmongodb.data.entity.UserEntity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

public class CustomUserDetailsService implements UserDetailsService {
    UserRepository repository;

    public CustomUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity entity = repository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with name: %s doesn't exist!", username)));
        String[] roles = entity.getRoles().stream().map(RoleStatus::name)
                .toArray(String[]::new);

        return User.builder()
                .username(entity.getEmail())
                .password(entity.getPassword())
                .authorities(AuthorityUtils.createAuthorityList(roles))
                .build();
    }
}
