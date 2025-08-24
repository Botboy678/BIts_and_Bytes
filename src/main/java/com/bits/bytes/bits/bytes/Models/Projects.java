package com.bits.bytes.bits.bytes.Models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.Set;


@Entity @Getter @Setter
public class Projects {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer project_id;

    @ManyToOne @JoinColumn(name = "user_id", nullable = false)
    @JsonManagedReference(value = "project")
    @JsonIgnore
    private Users userId;

    @Column(nullable = false)
    private String title;

    private String description;

    private String github_repo_url;

    @CreationTimestamp
    private LocalDateTime created_at;


    @OneToMany(mappedBy = "projectId", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "comments")
    private Set<ProjectComments> comments;

}
