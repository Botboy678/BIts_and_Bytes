package com.bits.bytes.bits.bytes.DTOs;

import com.bits.bytes.bits.bytes.Models.BugReports;
import com.bits.bytes.bits.bytes.Models.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class BugReportsDTO {
    private Integer userId;
    private String description;
    private LocalDateTime created_at;
    public enum Status {
        open, in_progress, resolved
    }

    private BugReports.Status status = BugReports.Status.open;
}
