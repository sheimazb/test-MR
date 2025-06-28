package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "solutions")
public class Solution {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "ticket_id", nullable = false)
    private Long ticketId;
    
    @Column(name = "solution_code", columnDefinition = "TEXT", nullable = false)
    private String solutionCode;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "file_path")
    private String filePath;
    
    @Column(name = "branch_name")
    private String branchName;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    // Constructors
    public Solution() {}
    
    public Solution(Long ticketId, String solutionCode, String description) {
        this.ticketId = ticketId;
        this.solutionCode = solutionCode;
        this.description = description;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getTicketId() { return ticketId; }
    public void setTicketId(Long ticketId) { this.ticketId = ticketId; }
    
    public String getSolutionCode() { return solutionCode; }
    public void setSolutionCode(String solutionCode) { this.solutionCode = solutionCode; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    
    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
} 