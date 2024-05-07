package com.bozdemir.clodious.service;

import com.bozdemir.clodious.DAO.UserDAO;
import com.bozdemir.clodious.model.ERole;
import com.bozdemir.clodious.model.User;
import com.bozdemir.clodious.payload.SigninRequest;
import com.bozdemir.clodious.payload.SignupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {
    @Lazy
    private final AuthenticationManager authenticationManager;
    private UserDAO userDAO;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserDAO userDAO, BCryptPasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.passwordEncoder = passwordEncoder;
        this.userDAO = userDAO;
        this.authenticationManager = authenticationManager;
    }

    public Boolean exitsUser(String username, String email) {
        Boolean exits = false;
        if (userDAO.existsByEmail(email) || userDAO.existsByUsername(username)) {
            exits = true;
        }
        return exits;
    }

    public Boolean createUser(SignupRequest request) {
        User newUser = User.UserBuilder.anUser()
                .name(request.name())
                .surname(request.surname())
                .email(request.email())
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .roles(Set.of(ERole.ROLE_USER))
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .isEnabled(true)
                .accountNonLocked(true)
                .build();

        return userDAO.saveOrUpdateUser(newUser);
    }

    public User signIn(SigninRequest request) throws EntityNotFoundException {

        Optional<User> user = userDAO.getUserByUsername(request.username());
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        } else {
            Authentication authentication = new UsernamePasswordAuthenticationToken(request.username(), request.password());
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String username = (String) authentication.getPrincipal();
            return user.get();
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userDAO.getUserByUsername(username);
        return user.orElseThrow(EntityNotFoundException::new);
    }
}
