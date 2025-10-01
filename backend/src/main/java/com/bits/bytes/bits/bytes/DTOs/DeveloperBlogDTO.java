package com.bits.bytes.bits.bytes.DTOs;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class DeveloperBlogDTO {
    private String title;
    private String description;
    private LocalDateTime datePublished;
    private String author;
    private List<DeveloperBlogCommentsDTO> blogComments;

}
