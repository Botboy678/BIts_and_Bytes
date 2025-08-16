package com.bits.bytes.bits.bytes.Models;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.Set;


@Entity @Getter @Setter
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer user_id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password_hash;

    @CreationTimestamp
    private LocalDateTime registered_at;

    @CreationTimestamp
    private LocalDateTime last_visit_at;

    @Column(columnDefinition = "boolean default false")
    private Boolean is_online;

    @Column(columnDefinition = "integer default 0")
    private Integer ability_points;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Profiles profile;

    @OneToMany(mappedBy = "userId")
    private Set<Friends> SentFriendRequests;

    @OneToMany(mappedBy = "friendUserId")
    private Set<Friends> ReceivedFriendRequests;

    @OneToMany(mappedBy = "userId")
    private Set<Projects> projects;

    @OneToMany(mappedBy = "userId")
    private Set<ProjectComments> comments;

    @OneToMany(mappedBy = "userId")
    private Set<BugReports> reports;
}
