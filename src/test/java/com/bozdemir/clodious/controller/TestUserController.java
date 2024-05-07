package com.bozdemir.clodious.controller;

import com.bozdemir.clodious.model.User;
import com.bozdemir.clodious.payload.SigninRequest;;
import com.bozdemir.clodious.payload.SignupRequest;
import com.bozdemir.clodious.payload.UserResponse;
import com.bozdemir.clodious.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.mockito.Mockito.when;

public class TestUserController {
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSignin() throws Exception {
        SigninRequest signinRequest = new SigninRequest("username", "password");

        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setUsername("username");
        user.setName("John");
        user.setSurname("Doe");

        when(userService.signIn(signinRequest)).thenReturn(user);

        ResponseEntity<?> responseEntity = userController.signin(signinRequest);

        Assertions.assertEquals(user.getId(), ((UserResponse) responseEntity.getBody()).id());
        Assertions.assertEquals(user.getEmail(), ((UserResponse) responseEntity.getBody()).email());
        Assertions.assertEquals(user.getUsername(), ((UserResponse) responseEntity.getBody()).username());
        Assertions.assertEquals(user.getName(), ((UserResponse) responseEntity.getBody()).name());
        Assertions.assertEquals(user.getSurname(), ((UserResponse) responseEntity.getBody()).surname());
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testSignup() throws Exception {
        SignupRequest signupRequest = new SignupRequest("username", "John", "Doe", "test@example", "password");

        when(userService.exitsUser(signupRequest.username(), signupRequest.email())).thenReturn(false);
        when(userService.createUser(signupRequest)).thenReturn(true);

        ResponseEntity<?> responseEntity = userController.signup(signupRequest);

        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        Assertions.assertTrue((Boolean) responseEntity.getBody());
    }
}
