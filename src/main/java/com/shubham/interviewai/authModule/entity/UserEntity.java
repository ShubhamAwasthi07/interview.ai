package com.shubham.interviewai.authModule.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Table(name = "users")
@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer uid;
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    private LocalDateTime createdAt = LocalDateTime.now();

}
