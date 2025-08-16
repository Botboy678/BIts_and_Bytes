package com.bits.bytes.bits.bytes.Models;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;


@Entity @Getter @Setter
public class ProjectComments {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer comment_id;

    @ManyToOne @JoinColumn(name = "user_id")
    private Users userId;

    @ManyToOne @JoinColumn(name = "project_id")
    private Projects projectId;

    private String Content;

    @CreationTimestamp
    private LocalDateTime created_at;
}
