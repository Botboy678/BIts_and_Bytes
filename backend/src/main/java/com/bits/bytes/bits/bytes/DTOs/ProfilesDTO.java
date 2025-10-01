package com.bits.bytes.bits.bytes.DTOs;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProfilesDTO {
    private String github_url;
    private String profile_photo_url;
    private String leetcode_username;
    private Integer leetcode_problems_solved;
    private String linkedin_url;
}
