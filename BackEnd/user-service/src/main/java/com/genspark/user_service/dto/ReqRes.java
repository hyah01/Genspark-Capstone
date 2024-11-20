package com.genspark.user_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.genspark.user_service.entities.User;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class ReqRes {

    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String role;
    private Integer reward_points;
    private List<String> orderHistory_ids;
    private User user;
    private List<User> userList;

}
