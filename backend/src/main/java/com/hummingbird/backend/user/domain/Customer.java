package com.hummingbird.backend.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hummingbird.backend.order.domain.Order;
import com.hummingbird.backend.user.dto.CustomerDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer extends User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id", nullable = false)
    private Long id;

    @Column(name = "token",nullable = false)
    private String token;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "name",nullable = false)
    private String name;

    @Builder
    public Customer(String token, String email, String password, String name) {
        //todo add token later
        this.token = name+"token";
        this.email = email;
        this.password = password;
        this.name = name;
    }
}