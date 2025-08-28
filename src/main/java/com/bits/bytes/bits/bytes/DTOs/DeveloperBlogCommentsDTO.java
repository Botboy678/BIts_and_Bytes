package com.bits.bytes.bits.bytes.DTOs;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeveloperBlogCommentsDTO {
    private String Description;
    private DeveloperBlogDTO blogId;
    private UsersDTO userId;
}
