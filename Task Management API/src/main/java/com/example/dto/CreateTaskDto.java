package com.example.dto;

import lombok.Data;

@Data
public class CreateTaskDto {
    private String name;

    private String description;

    private Boolean completed;

    private Integer userId;
}
