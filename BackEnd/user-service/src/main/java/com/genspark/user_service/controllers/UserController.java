package com.genspark.user_service.controllers;

import com.genspark.user_service.dto.ReqRes;
import com.genspark.user_service.entities.User;
import com.genspark.user_service.services.UserInfoDetails;
import com.genspark.user_service.services.UserInfoService;
import com.genspark.user_service.services.UserManagementService;
import com.genspark.user_service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserManagementService userManagementService;
    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("/admin/get-all-users")
    public ResponseEntity<ReqRes> getAllUsers(){
        return ResponseEntity.ok(userManagementService.getAllUsers());
    }


    @GetMapping("/admin/get-user/{userId}")
    public ResponseEntity<ReqRes> getUserById(@PathVariable String userId){
        return ResponseEntity.ok(userManagementService.getUserById(userId));
    }

    @PutMapping("/admin/update/{userId}")
    public ResponseEntity<ReqRes> updateUser(@PathVariable String userId, @RequestBody User user){
        return ResponseEntity.ok(userManagementService.updateUser(userId, user));
    }

    @GetMapping("/adminuser/get-profile")
    public ResponseEntity<ReqRes> getMyProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        ReqRes response = userManagementService.getMyInfo(email);
        return  ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/admin/delete/{userId}")
    public ResponseEntity<ReqRes> deleteUSer(@PathVariable String userId){
        return ResponseEntity.ok(userManagementService.deleteUser(userId));
    }

    // TO DO ADD DELETE USER BY USER


}
