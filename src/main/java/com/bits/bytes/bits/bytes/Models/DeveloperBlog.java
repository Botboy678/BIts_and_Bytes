package com.bits.bytes.bits.bytes.Models;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity @Getter @Setter
public class DeveloperBlog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    private String title;

    private String Description;

    @CreationTimestamp
    private LocalDateTime datePublished;

    @Column(columnDefinition = "varchar(255) default 'August'")
    private String author;

    @OneToMany(mappedBy = "blogId")
    private List<DeveloperBlogComments> blogComments;
}
