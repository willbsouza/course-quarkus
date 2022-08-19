package com.github.quarkussocial.resources.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreatePostRequest {

    @NotBlank(message = "Text is required.")
    private String text;
}
