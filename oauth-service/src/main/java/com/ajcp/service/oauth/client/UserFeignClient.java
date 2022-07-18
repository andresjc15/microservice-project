package com.ajcp.service.oauth.client;

import com.ajcp.service.common.user.model.entity.UserModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service")
public interface UserFeignClient {

    @GetMapping("/users/search/getPerUsername")
    public UserModel findByUsername(@RequestParam String username);

    @PutMapping("/users/{id}")
    public UserModel update(@RequestBody UserModel user, @PathVariable Long id);

}
