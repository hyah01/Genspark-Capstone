package com.genspark.user_service.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String role;
    private Integer reward_points;
    private List<String> orderHistory_ids;


}
