package com.bits.bytes.bits.bytes.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfilesDTO {
    private String github_url;
    private String profile_photo_url;
    private String leetcode_username;
    private Integer leetcode_problems_solved;
    private String linkedin_url;
}
