package com.github.quarkussocial.resources.dto;

import com.github.quarkussocial.domain.model.Post;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jboss.logging.annotations.Pos;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PostResponse {

    private String text;
    private LocalDateTime dateTime;

    public PostResponse(Post post){
        this.text = post.getText();
        this.dateTime = post.getDateTime();
    }
}
