package com.genspark.user_service.controllers;


import brave.Response;
import com.genspark.user_service.dto.ReqRes;
import com.genspark.user_service.entities.AuthRequest;
import com.genspark.user_service.entities.User;
import com.genspark.user_service.repositories.UserRepository;
import com.genspark.user_service.services.UserManagementService;
import com.genspark.user_service.services.UserService;
import com.genspark.user_service.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
//@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserManagementService userManagementService;
    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    UserRepository userRepository;



    @PostMapping("/signup")
    public ResponseEntity<ReqRes> signup(@RequestBody ReqRes reg) {
        return ResponseEntity.ok(userManagementService.register(reg));
    }


    @PostMapping("/login")
    public ResponseEntity<ReqRes> login(@RequestBody ReqRes reg) {
        System.out.println(1);
        return ResponseEntity.ok(userManagementService.login(reg));
    }
    @GetMapping("/email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        boolean exists = userService.emailExists(email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token){
        userManagementService.validateToken(token);
        return "valid";
    }






}
