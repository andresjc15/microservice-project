package com.ajcp.service.oauth.security.event;

import com.ajcp.service.common.user.model.entity.UserModel;
import com.ajcp.service.oauth.service.UserService;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationErrorHandler implements AuthenticationEventPublisher {

    @Autowired
    private UserService userService;

    @Override
    public void publishAuthenticationSuccess(Authentication authentication) {
        if(authentication.getDetails() instanceof WebAuthenticationDetails) {
            return;
        }

        UserDetails user = (UserDetails) authentication.getPrincipal();
        String message = "Success login: " + user.getUsername();
        log.info(message);

        UserModel userModel = userService.findByUsername(authentication.getName());
        if (userModel.getAttempts() != null && userModel.getAttempts() > 0) {
            userModel.setAttempts(0);
            userService.update(userModel, userModel.getId());
        }
    }

    @Override
    public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
        String message = "Error in login: " + exception.getMessage();
        log.info(message);

        try {
            UserModel user = userService.findByUsername(authentication.getName());
            if (user.getAttempts() == null) {
                user.setAttempts(0);
            }
            log.info("Actual attempt is: " + user.getAttempts());
            user.setAttempts(user.getAttempts() +  1);
            log.info("After attempt is: " + user.getAttempts());
            if (user.getAttempts() >= 3) {
                log.error(String.format("User %s disabled for multiple attempts", user.getUsername()));
                user.setEnabled(false);
            }
            userService.update(user, user.getId());
        } catch (FeignException e) {
            log.error(String.format("User doesn't exist in the system ", authentication.getName()));
        }
    }
}
