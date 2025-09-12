package com.bits.bytes.bits.bytes.DTOs;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DeveloperBlogCommentsDTO {
    private String description;
    private Integer blogId;
    private Integer userId;
}
