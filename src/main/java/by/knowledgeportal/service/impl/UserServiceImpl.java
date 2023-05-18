package by.knowledgeportal.service.impl;

import by.knowledgeportal.entity.User;
import by.knowledgeportal.entity.enums.Role;
import by.knowledgeportal.repository.UserRepository;
import by.knowledgeportal.security.Auth;
import by.knowledgeportal.security.SecurityUser;
import by.knowledgeportal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;


    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User save(User user) {
        user.getRoles().add(Role.USER);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    @Override
    public String getEmailFromSecurity() {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } else {
            return null;
        }
    }

    @Override
    public void update(User user, UserDetails userDetails) {
        if (!user.getEmail().equals(userDetails.getUsername()) && existsByEmail(user.getEmail())) {
            return;
        }
        User userDb = findByEmail(userDetails.getUsername());
        if (!userDb.getPassword().equals(user.getPassword())) {
            userDb.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        userDb.setEmail(user.getEmail());
        userDb.setUsername(user.getUsername());
        userRepository.save(userDb);
        updateUserSecurity(userDb, userDetails);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }


    private void updateUserSecurity(User user, UserDetails userDetails) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails currentUserDetails = (UserDetails) authentication.getPrincipal();
        if (currentUserDetails.getUsername().equals(userDetails.getUsername())) {
            UserDetails updatedUserDetails = SecurityUser.fromUser(user);
            Authentication newAuthentication = new UsernamePasswordAuthenticationToken(updatedUserDetails, authentication.getCredentials(), updatedUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(newAuthentication);
        }
    }
}
