package com.bits.bytes.bits.bytes.Models;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity @Getter @Setter
public class DeveloperBlogComments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    private String Description;

    @ManyToOne
    @JoinColumn(name = "blog_id")
    private DeveloperBlog blogId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users userId;
}
