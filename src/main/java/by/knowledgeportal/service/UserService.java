package by.knowledgeportal.service;

import by.knowledgeportal.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {

    User findByEmail(String email);
    User save(User user);
    String getEmailFromSecurity();

    void update(User user, UserDetails userDetails);

    boolean existsByEmail(String email);

    List<User> findAll();
}
