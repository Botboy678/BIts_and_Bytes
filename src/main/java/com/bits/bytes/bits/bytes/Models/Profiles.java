package com.bits.bytes.bits.bytes.Models;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity @Getter @Setter
public class Profiles {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer profile_id;

    @OneToOne @JoinColumn(name = "users_user_id")
    private Users user;

    private String profile_photo_url;
    private String github_url;
    private String leetcode_username;
    private String linkedin_url;

    @Column(columnDefinition = "integer default 0")
    private Integer leetcode_problems_solved;

}
