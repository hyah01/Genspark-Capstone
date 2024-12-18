package com.genspark.user_service.services;

import com.genspark.user_service.dto.ReqRes;
import com.genspark.user_service.entities.User;
import com.genspark.user_service.repositories.UserRepository;
import com.genspark.user_service.util.JwtUtil;
import com.netflix.discovery.converters.Auto;
import org.bouncycastle.crypto.OutputLengthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;


@Service
public class UserManagementService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserManagementService.class);

    public ReqRes register(ReqRes registrationRequest){
        ReqRes resp = new ReqRes();
        try {
            User user = new User();
            user.setEmail(registrationRequest.getEmail());
            if (!(userService.isValid(registrationRequest.getPassword()))){
                throw new IllegalAccessException("Password is invalid");
            }
            user.setFirst_name(registrationRequest.getFirst_name());
            user.setLast_name(registrationRequest.getLast_name());
            user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            user.setRole(registrationRequest.getRole());
            user.setReward_points(registrationRequest.getReward_points());
            user.setOrderHistory_ids(registrationRequest.getOrderHistory_ids());
            User userResult = userRepository.save(user);
            if (userResult.getId() != null) {
                resp.setUser(userResult);
                resp.setMessage("User Saved Successfully");
                resp.setStatusCode(200);

            }

        } catch (IllegalArgumentException e) {
            resp.setStatusCode(400); // Bad Request
            resp.setError(e.getMessage());
        } catch (Exception e){
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public ReqRes login(ReqRes logginRequest){
        logger.info("Getting into login");
        ReqRes response = new ReqRes();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(logginRequest.getEmail(), logginRequest.getPassword()));
            var user = userRepository.findByEmail(logginRequest.getEmail()).orElseThrow();
            var jwt = jwtUtil.generateToken(user.getEmail(), Arrays.asList(user.getRole()));
            var refreshToken = jwtUtil.generateToken(user.getEmail(),Arrays.asList(user.getRole()));
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRole(user.getRole());
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hrs");
            response.setMessage("Successfully Logged In");

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    public ReqRes refreshToken(ReqRes refreshTokenRequest){
        ReqRes response = new ReqRes();
        try {
            String email = jwtUtil.extractUsername(refreshTokenRequest.getToken());
            User user = userRepository.findByEmail(email).orElseThrow();
            if (jwtUtil.validateToken(refreshTokenRequest.getToken(), user)) {
                var jwt = jwtUtil.generateToken(user.getEmail(),Arrays.asList(user.getRole()));
                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRefreshToken(refreshTokenRequest.getToken());
                response.setExpirationTime("24Hrs");
                response.setMessage("Successfully Logged In");
            }
            response.setStatusCode(200);


        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    public ReqRes getAllUsers(){
        ReqRes reqRes = new ReqRes();
        try {
            List<User> result = userRepository.findAll();
            if (!result.isEmpty()) {
                reqRes.setUserList(result);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("No users found");
            }


        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error Occurred:" + e.getMessage());
        }
        return reqRes;
    }



    public ReqRes getUserById(String id){
        ReqRes reqRes = new ReqRes();
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not Found"));
            reqRes.setUser(user);
            reqRes.setStatusCode(200);
            reqRes.setMessage("User with id '" + id + "' found successfully");
        } catch (Exception e){
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error Occurred: " + e.getMessage());
        }
        return  reqRes;
    }

    public ReqRes deleteUser (String id){
         ReqRes reqRes = new ReqRes();
         try {
             Optional<User> userOptional = userRepository.findById(id);
             if (userOptional.isPresent()){
                 userRepository.deleteById(id);
                 reqRes.setStatusCode(200);
                 reqRes.setMessage("User Deleted Successfully");
             } else {
                 reqRes.setStatusCode(404);
                 reqRes.setMessage("User not found for deletion");
             }
         } catch (Exception e) {
             reqRes.setStatusCode(500);
             reqRes.setMessage("Error Occurred while deleting user: " + e.getMessage());
         }
         return reqRes;
    }

    public ReqRes updateUser (String id, User updatedUser){
        ReqRes reqRes = new ReqRes();
        try {
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isPresent()){
                User user = userOptional.get();
                user.setEmail(updatedUser.getEmail());
                user.setFirst_name(updatedUser.getFirst_name());
                user.setLast_name(updatedUser.getLast_name());
                user.setRole(updatedUser.getRole());
                user.setReward_points(updatedUser.getReward_points());
                user.setOrderHistory_ids(updatedUser.getOrderHistory_ids());

                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                    user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                }

                User savedUser = userRepository.save(user);
                reqRes.setUser(savedUser);
                reqRes.setStatusCode(200);
                reqRes.setMessage("User Updated Successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error Occurred while getting user info: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes getMyInfo(String email){
        ReqRes reqRes = new ReqRes();
        try {
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                reqRes.setUser(userOptional.get());
                reqRes.setStatusCode(200);
                reqRes.setMessage("successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
            }

        }catch (Exception e){
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while getting user info: " + e.getMessage());
        }
        return reqRes;

    }

    public void validateToken(String token){
        jwtUtil.tokenValidate(token);
    }


}
