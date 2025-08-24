package com.bits.bytes.bits.bytes.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;


@Entity @Setter @Getter
public class BugReports {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bug_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "bug_report")
    private Users userId;

    @Column(nullable = false)
    private String description;

    @CreationTimestamp
    private LocalDateTime created_at;

    public enum Status {
        open, in_progress, resolved
    }

    @Enumerated(EnumType.STRING)
    private Status status = Status.open;
}
