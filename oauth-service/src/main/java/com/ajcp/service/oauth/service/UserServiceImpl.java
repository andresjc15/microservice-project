package com.ajcp.service.oauth.service;

import com.ajcp.service.common.user.model.entity.UserModel;
import com.ajcp.service.oauth.client.UserFeignClient;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired
    private UserFeignClient client;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        try {
            UserModel user = client.findByUsername(username);

            List<GrantedAuthority> authorities = user.getRoles()
                    .stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .peek(authority -> log.info("Role: " + authority.getAuthority()))
                    .collect(Collectors.toList());

            log.info("User authenticated successfully");

            return new User(user.getUsername(), user.getPassword(), user.isEnabled(), true,
                    true, true, authorities);
        } catch (FeignException e) {
            log.error("[ERROR]: Error in login, User doesn't exist");
            throw new UsernameNotFoundException("Error in login, User doesn't exist");
        }

    }

    @Override
    public UserModel findByUsername(String username) {
        return client.findByUsername(username);
    }

    @Override
    public UserModel update(UserModel user, Long id) {
        return client.update(user, id);
    }
}
