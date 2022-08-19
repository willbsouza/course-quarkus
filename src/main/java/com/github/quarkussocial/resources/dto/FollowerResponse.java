package com.github.quarkussocial.resources.dto;

import com.github.quarkussocial.domain.model.Follower;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FollowerResponse {
    private Long id;
    private String name;

    public FollowerResponse(Follower follower){
        this.id = follower.getId();
        this.name = follower.getFollower().getName();
    }
}
