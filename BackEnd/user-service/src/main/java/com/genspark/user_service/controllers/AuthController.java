package com.genspark.user_service.controllers;


import com.genspark.user_service.entities.AuthRequest;
import com.genspark.user_service.entities.User;
import com.genspark.user_service.services.UserService;
import com.genspark.user_service.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;



    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody User user) {
        return ResponseEntity.ok(userService.saveUser(user));
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest, HttpServletResponse response) throws AuthenticationException {
        // Authenticate the user using the provided credentials
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        if (authentication.isAuthenticated()) {
            // Generate a JWT token for the authenticated user
            String token = jwtUtil.generateToken(authRequest.getUsername());

            // Create an HTTP-only cookie with the JWT token
            Cookie cookie = new Cookie("JWT", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            return ResponseEntity.ok("Login successful, JWT token set in HTTP-only cookie");
        } else {
            throw new RuntimeException("Invalid login details");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // Clear the JWT cookie
        Cookie cookie = new Cookie("JWT", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // Set cookie to expire immediately
        response.addCookie(cookie);

        return ResponseEntity.ok("Logout successful");
    }
}
