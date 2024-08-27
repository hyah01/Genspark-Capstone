package com.genspark.user_service.controllers;

import com.genspark.user_service.entities.User;
import com.genspark.user_service.services.UserInfoDetails;
import com.genspark.user_service.services.UserInfoService;
import com.genspark.user_service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserInfoService userInfoService;

//  Create a new user.
    @PostMapping("/add")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.saveUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

//  Retrieves a user by ID.
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//  Retrieves all users.
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        boolean exists = userService.emailExists(email);
        return ResponseEntity.ok(exists);
    }

//  Updates an existing user.
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        if (updatedUser != null) {
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//  Deletes a user by ID.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        boolean isDeleted = userService.deleteUser(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
