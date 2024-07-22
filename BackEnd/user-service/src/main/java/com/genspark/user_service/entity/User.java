package com.genspark.user_service.entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    String userId;
    String email;
    String password;
    String firstName;
    String lastName;
    List<Long> orders;
    String role;
    String rewardPoint;
    String cartId;

//    public User(String userId,
//    String email, String password, String firstName, String lastName,
//    ArrayList<Long> orders,
//     String role, String rewardPoint, String cartId) {
//        this.userId = userId;
//        this.email = email;
//        this.password = password;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.orders = orders;
//        this.role = role;
//        this.rewardPoint = rewardPoint;
//        this.cartId = cartId;
//    }
}
