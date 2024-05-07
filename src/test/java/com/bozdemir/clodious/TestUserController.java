package com.bozdemir.clodious;

import com.bozdemir.clodious.controller.UserController;
import com.bozdemir.clodious.payload.SignupRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import com.bozdemir.clodious.config.PasswordEncoderConfig;
import com.bozdemir.clodious.security.SecurityConfig;
import com.bozdemir.clodious.security.WebConfig;

import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {PasswordEncoderConfig.class, WebConfig.class, SecurityConfig.class})
@Transactional
@WebAppConfiguration
public class TestUserController {
    @Autowired
    private UserController userController;

    @Test
    @Rollback(value = false)
    public void testSingUp() {
        SignupRequest signupRequest = new SignupRequest("tahabozdemir", "taha", "bozdemir", "taha@bozdemir.com", "taha123");
        ResponseEntity isCreated = userController.signup(signupRequest);
        Assertions.assertTrue(isCreated.getStatusCode().is2xxSuccessful());
    }
}
