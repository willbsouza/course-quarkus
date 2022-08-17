package com.github.quarkussocial.domain.model;

import com.github.quarkussocial.resources.dto.CreateConsumerRequest;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Consumer extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer age;

    public Consumer(CreateConsumerRequest consumerRequest){
        this.name = consumerRequest.getName();
        this.age = consumerRequest.getAge();
    }
}
