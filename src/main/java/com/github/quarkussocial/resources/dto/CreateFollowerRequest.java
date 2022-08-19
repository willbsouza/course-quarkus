package com.github.quarkussocial.resources.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class CreateFollowerRequest {
    @NotBlank(message = "FollowerId is required.")
    private Long followerId;
}
