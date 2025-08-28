package com.bits.bytes.bits.bytes.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class DeveloperBlogDTO {
    private String title;
    private String Description;
    private LocalDateTime datePublished;
    private String author;
    private List<DeveloperBlogCommentsDTO> blogComments;

}
