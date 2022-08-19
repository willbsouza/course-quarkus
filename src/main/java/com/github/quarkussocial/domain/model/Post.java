package com.github.quarkussocial.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "post_text", nullable = false)
    private String text;

    @Column(name = "datetime", nullable = false)
    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "consumer_id")
    private Consumer consumer;

    public Post(String text, LocalDateTime dateTime, Consumer consumer) {
        this.text = text;
        this.dateTime = dateTime;
        this.consumer = consumer;
    }
}
