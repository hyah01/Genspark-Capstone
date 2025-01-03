package com.genspark.cart_service.services;

import com.genspark.user_service.entities.User;
import com.genspark.user_service.services.UserInfoDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartUserInfoService implements UserDetailsService {

    @Autowired
    private UserClient repository;

    @Autowired
    private PasswordEncoder encoder;

    // Methods to simply load user's data
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> userDetail = repository.loadUser(username);

        if (userDetail.isPresent()){
            System.out.println(userDetail.get().getEmail());
        }

        // Converting userDetail to UserDetails
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
    }
}
