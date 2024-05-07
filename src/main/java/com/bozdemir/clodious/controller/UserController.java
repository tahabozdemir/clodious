package com.bozdemir.clodious.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.bozdemir.clodious.model.User;
import com.bozdemir.clodious.payload.MessageResponse;
import com.bozdemir.clodious.payload.SigninRequest;
import com.bozdemir.clodious.payload.SignupRequest;
import com.bozdemir.clodious.payload.UserResponse;
import com.bozdemir.clodious.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*", exposedHeaders = "Authorization")
@RestController
@RequestMapping("/auth")
public class UserController {
    @Autowired
    UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest signupRequest) {

        if (userService.exitsUser(signupRequest.username(), signupRequest.password())) {

            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Username already exists"));
        } else {
            Boolean isCreated = userService.createUser(signupRequest);
            if (isCreated) {
                logger.info("User created:{}", signupRequest.username());
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(isCreated);
            } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(isCreated);
            }
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@Valid @RequestBody SigninRequest signinRequest) {
        User user = null;
        try {
            user = userService.signIn(signinRequest);
            logger.info("User signin: {}", user.getUsername());
            return ResponseEntity
                    .ok()
                    .body(new UserResponse(user.getId(), user.getEmail(), user.getUsername(), user.getName(), user.getSurname()));
        } catch (EntityNotFoundException e) {
            logger.info("Attempt login: {}", signinRequest.username());
        }
        return ResponseEntity
                .ok()
                .body("User not found");
    }
}
