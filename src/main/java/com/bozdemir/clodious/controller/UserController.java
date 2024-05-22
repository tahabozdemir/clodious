package com.bozdemir.clodious.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.bozdemir.clodious.model.User;
import com.bozdemir.clodious.payload.response.MessageResponse;
import com.bozdemir.clodious.payload.request.SigninRequest;
import com.bozdemir.clodious.payload.request.SignupRequest;
import com.bozdemir.clodious.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.persistence.EntityNotFoundException;

@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*", exposedHeaders = "Authorization")
@Controller
@RequestMapping("/auth")
public class UserController {
    @Autowired
    UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @ResponseBody
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest signupRequest) {
        if (userService.exitsUser(signupRequest.username(), signupRequest.password())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new MessageResponse("Username already exists"));
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
    public String signin(@Valid @ModelAttribute("signinRequest") SigninRequest signinRequest, BindingResult bindingResult, Model model) {
        User user = null;
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", true);
            return "signin";
        }
        try {
            user = userService.signIn(signinRequest);
            logger.info("User signin: {}", user.getUsername());
        } catch (EntityNotFoundException e) {
            logger.info("Attempt login: {}", signinRequest.username());
        }
        if (user == null) {
            model.addAttribute("error", true);
            return "signin";
        }
        return "redirect:/files";
    }

    @GetMapping("/signin")
    public String signin(Model model) {
        model.addAttribute("signinRequest", new SigninRequest(null, null));
        return "signin";
    }

    @PostMapping("/signout")
    public String singout(HttpServletRequest request, HttpServletResponse response, Model model) {
        userService.signOut(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "signin";
    }
}
