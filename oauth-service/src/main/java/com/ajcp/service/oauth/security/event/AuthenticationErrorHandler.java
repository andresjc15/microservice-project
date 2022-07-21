package com.ajcp.service.oauth.security.event;

import brave.Tracer;
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
    private Tracer tracer;

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
            StringBuilder errors = new StringBuilder();
            errors.append(message);

            UserModel user = userService.findByUsername(authentication.getName());
            if (user.getAttempts() == null) {
                user.setAttempts(0);
            }
            log.info("Actual attempt is: " + user.getAttempts());
            user.setAttempts(user.getAttempts() +  1);
            log.info("After attempt is: " + user.getAttempts());

            errors.append("- Attempts login: " + user.getAttempts());

            if (user.getAttempts() >= 3) {
                String errorMaxAttempt = String.format("User %s disabled for multiple attempts", user.getUsername());
                log.error(errorMaxAttempt);
                errors.append("- " + errorMaxAttempt);
                user.setEnabled(false);
            }
            userService.update(user, user.getId());

            tracer.currentSpan().tag("error.message", errors.toString());
        } catch (FeignException e) {
            log.error(String.format("User doesn't exist in the system ", authentication.getName()));
        }
    }
}
