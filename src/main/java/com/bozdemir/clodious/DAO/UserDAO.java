package com.bozdemir.clodious.DAO;
import com.bozdemir.clodious.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface UserDAO {
    Optional<User> getUserByUsername(String username);
    Boolean saveOrUpdateUser(User user);
    Boolean removeUser(User user);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
