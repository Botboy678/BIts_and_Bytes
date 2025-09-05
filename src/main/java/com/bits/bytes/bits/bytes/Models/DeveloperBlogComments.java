package com.bits.bytes.bits.bytes.Models;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity @Getter @Setter
public class DeveloperBlogComments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer Id;

    private String description;

    @ManyToOne
    @JoinColumn(name = "blog_id")
    @JsonBackReference(value = "blogComments")
    private DeveloperBlog blogId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonManagedReference(value = "developerBlogComment")
    @JsonIgnore
    private Users user;
}
