package com.genspark.user_service.controllers;

import com.genspark.user_service.dto.ReqRes;
import com.genspark.user_service.entities.User;
import com.genspark.user_service.services.UserInfoDetails;
import com.genspark.user_service.services.UserInfoService;
import com.genspark.user_service.services.UserManagementService;
import com.genspark.user_service.services.UserService;
import com.genspark.user_service.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserManagementService userManagementService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private JwtUtil jwtUtil;

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

    @GetMapping("/get-User")
    public ResponseEntity<ReqRes> getUser(@RequestHeader(HttpHeaders.AUTHORIZATION)String token){
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            // Validate the token
            String email = jwtUtil.extractUsername(token);
            System.out.println(email);
            if (email == null || jwtUtil.token(token)) {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
            }
            return ResponseEntity.ok(userManagementService.getMyInfo(email));
        }catch(Exception e){
            e.printStackTrace(); // Print stack trace for debugging
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    @PutMapping("/adminuser/uploadProfileImage")
    public ResponseEntity<String> uploadProfileImage(@RequestParam("image")MultipartFile image, @RequestHeader(HttpHeaders.AUTHORIZATION)String token){
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            jwtUtil.tokenValidate(token);
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {

            // Check if the file is empty
            if (image.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No file uploaded");
            }

            userManagementService.uploadProfileImage(image);

            return ResponseEntity.status(HttpStatus.OK).body("File uploaded successfully: " + image.getOriginalFilename());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file");
        }
    }
    // TO DO ADD DELETE USER BY USER


}
