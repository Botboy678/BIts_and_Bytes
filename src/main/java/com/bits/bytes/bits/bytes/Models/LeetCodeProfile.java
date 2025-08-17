package com.bits.bytes.bits.bytes.Models;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class LeetCodeProfile {
    private int totalSolved;
    private int totalQuestions;
    private int easySolved;
    private int totalEasy;
    private int mediumSolved;
    private int totalMedium;
    private int hardSolved;
    private int totalHard;
    private int ranking;
    private int contributionPoint;
    private int reputation;
}
