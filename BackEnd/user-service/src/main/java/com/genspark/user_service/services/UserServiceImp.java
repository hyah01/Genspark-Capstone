package com.genspark.user_service.services;

import com.genspark.user_service.dto.ReqRes;
import com.genspark.user_service.entities.AuthRequest;
import com.genspark.user_service.entities.User;
import com.genspark.user_service.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.regex.Pattern;


@Service
public class UserServiceImp implements UserService{
    private static final String LENGTH_REGEX = ".{8,}";
    private static final String UPPERCASE_REGEX = ".*[A-Z].*";
    private static final String LOWERCASE_REGEX = ".*[a-z].*";
    private static final String DIGIT_REGEX = ".*\\d.*";
    private static final String SPECIAL_CHAR_REGEX = ".*[!@#$%^&*(),.?\":{}|<>].*";
    @Autowired
    private UserRepository userRepository;

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean isValid(String password){
        return Pattern.matches(LENGTH_REGEX, password)
                && Pattern.matches(UPPERCASE_REGEX, password)
                && Pattern.matches(LOWERCASE_REGEX, password)
                && Pattern.matches(DIGIT_REGEX, password)
                && Pattern.matches(SPECIAL_CHAR_REGEX, password);
    }




}

