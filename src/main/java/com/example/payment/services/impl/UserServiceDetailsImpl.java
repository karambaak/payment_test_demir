package com.example.payment.services.impl;

import com.example.payment.entities.User;
import com.example.payment.repositories.UserRepository;
import com.example.payment.services.UserServiceDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceDetailsImpl implements UserServiceDetails {

    private final UserRepository userRepository;
    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                User user = userRepository.findById(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                return user;
            }
        };
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findById(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//        return user;
//    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User userEntity = userRepository.findById(username).orElseThrow(
//                () -> new UsernameNotFoundException("User not found"));
//        return new org.springframework.security.core.userdetails.User(
//                userEntity.getUsername(),
//                userEntity.getPassword(),
//                userEntity.isEnabled(),
//                true,
//                true,
//                true,
//                userEntity.getAuthorities()
//        );
//    }


}
