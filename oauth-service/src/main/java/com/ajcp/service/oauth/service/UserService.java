package com.ajcp.service.oauth.service;

import com.ajcp.service.common.user.model.entity.UserModel;

public interface UserService {

    public UserModel findByUsername(String username);

    public UserModel update(UserModel user, Long id);

}
