package com.ajcp.service.oauth.security;

import com.ajcp.service.common.user.model.entity.UserModel;
import com.ajcp.service.oauth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AdditionalInfoToken implements TokenEnhancer {

    @Autowired
    private UserService userService;


    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        Map<String, Object> info = new HashMap<String, Object>();

        UserModel user = userService.findByUsername(oAuth2Authentication.getName());
        info.put("name", user.getName());
        info.put("lastname", user.getLastname());
        info.put("email", user.getEmail());
        ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(info);
        return oAuth2AccessToken;
    }
}
