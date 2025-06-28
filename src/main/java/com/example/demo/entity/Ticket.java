package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class Ticket {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status = TicketStatus.IN_PROGRESS;
    
    @Column(name = "solution_branch")
    private String solutionBranch;
    
    @Column(name = "pipeline_status")
    private String pipelineStatus;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    // Constructors
    public Ticket() {}
    
    public Ticket(String title, String description) {
        this.title = title;
        this.description = description;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public TicketStatus getStatus() { return status; }
    public void setStatus(TicketStatus status) { 
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getSolutionBranch() { return solutionBranch; }
    public void setSolutionBranch(String solutionBranch) { this.solutionBranch = solutionBranch; }
    
    public String getPipelineStatus() { return pipelineStatus; }
    public void setPipelineStatus(String pipelineStatus) { this.pipelineStatus = pipelineStatus; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
} 