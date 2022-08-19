package com.github.quarkussocial.domain.model;

import com.github.quarkussocial.resources.dto.CreateUserRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer age;

    public User(CreateUserRequest userRequest){
        this.name = userRequest.getName();
        this.age = userRequest.getAge();
    }
}
