package com.github.quarkussocial.resources.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateConsumerRequest {

    @NotBlank(message = "Name is Required.")
    private String name;

    @NotNull(message = "Age is Required.")
    private Integer age;
}
