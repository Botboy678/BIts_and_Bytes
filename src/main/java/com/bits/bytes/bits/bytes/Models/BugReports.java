package com.bits.bytes.bits.bytes.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;


@Entity @Setter @Getter
public class BugReports {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bug_id;

    @ManyToOne @JoinColumn(name = "user_id")
    private Users userId;

    private String description;

    @CreationTimestamp
    private String created_at;

    private enum Status {
        open, in_progress, resolved
    }

    @Enumerated(EnumType.STRING)
    private Status status = Status.open;
}
