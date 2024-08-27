package com.genspark.user_service.controllers;


import brave.Response;
import com.genspark.user_service.entities.AuthRequest;
import com.genspark.user_service.entities.User;
import com.genspark.user_service.services.UserService;
import com.genspark.user_service.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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

    @Autowired
    private UserDetailsService userDetailsService;



    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody User user) {
        return ResponseEntity.ok(userService.saveUser(user));
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest, HttpServletResponse response) throws AuthenticationException {
        // Authenticate the user using the provided credentials
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));

        if (authentication.isAuthenticated()) {
            // Generate a JWT token for the authenticated user
            String token = jwtUtil.generateToken(authRequest.getEmail());

            // Create an HTTP-only cookie with the JWT token
            Cookie cookie = new Cookie("JWT", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            // Return a JSON response
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Login successful");
            responseBody.put("status", 200);
            responseBody.put("token", token);  // Optional: If you
            return ResponseEntity.ok(responseBody);
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Invalid login details");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
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

    @GetMapping("/verify-token")
    public ResponseEntity<?> verifyToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = null;

        if (cookies != null) {
            for (Cookie cookie : cookies ){
                if ("JWT".equals(cookie.getName())){
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token != null) {
            // Extract username from token if any
            String username = jwtUtil.extractUsername(token);
            if (username != null){
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);


                if (jwtUtil.validateToken(token, userDetails)){

                    User user = userService.getUserByEmail(username);

                    Map<String, Object> response = new HashMap<>();
                    response.put("firstName", user.getFirst_name());
                    response.put("lastName", user.getLast_name());
                    response.put("email", user.getEmail());
                    response.put("role", user.getRole());
                    response.put("rewardPoints", user.getReward_points());
                    response.put("orderHistory", user.getOrderHistory_ids());
                    response.put("status", "Token is valid");

                    return ResponseEntity.ok(response);
                }
            }
        }
        // Token is invalid or not present
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("status", "error");
        errorResponse.put("message", "Token is invalid or expired");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }


}
