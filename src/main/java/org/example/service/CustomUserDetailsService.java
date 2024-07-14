package org.example.service;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
        if (!optionalUser.isPresent()) {
            throw new UsernameNotFoundException("User not found with phone number: " + phoneNumber);
        }
        User user = optionalUser.get();
        return new org.springframework.security.core.userdetails.User(user.getPhoneNumber(), user.getPassword(), user.getAuthorities());
    }
}
