package com.example.demo.service;

import com.example.demo.entity.Solution;
import com.example.demo.entity.Ticket;
import com.example.demo.entity.TicketStatus;
import com.example.demo.repository.SolutionRepository;
import com.example.demo.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SolutionService {
    
    @Autowired
    private SolutionRepository solutionRepository;
    
    @Autowired
    private TicketRepository ticketRepository;
    
    @Autowired
    private GitService gitService;
    
    @Transactional
    public Solution createSolution(Long ticketId, String solutionCode, String description) {
        // Verify ticket exists and is in progress
        Ticket ticket = ticketRepository.findById(ticketId)
            .orElseThrow(() -> new RuntimeException("Ticket not found"));
        
        if (ticket.getStatus() != TicketStatus.IN_PROGRESS) {
            throw new RuntimeException("Can only create solutions for tickets in IN_PROGRESS status");
        }
        
        try {
            // Create Git branch with solution
            String branchName = gitService.createBranchWithSolution(ticketId, solutionCode, description);
            
            // Save solution to database
            Solution solution = new Solution(ticketId, solutionCode, description);
            solution.setBranchName(branchName);
            solution.setFilePath("src/main/java/solutions/Ticket" + ticketId + "Solution.java");
            
            Solution savedSolution = solutionRepository.save(solution);
            
            // Update ticket status to RESOLVED and store branch name
            ticket.setStatus(TicketStatus.RESOLVED);
            ticket.setSolutionBranch(branchName);
            ticket.setPipelineStatus("branch_created");
            ticketRepository.save(ticket);
            
            System.out.println("✅ Solution created for ticket #" + ticketId);
            System.out.println("📝 Ticket status changed to RESOLVED");
            System.out.println("🌿 Branch created: " + branchName);
            
            return savedSolution;
            
        } catch (Exception e) {
            System.err.println("❌ Error creating solution: " + e.getMessage());
            throw new RuntimeException("Failed to create solution", e);
        }
    }
    
    public List<Solution> getSolutionsByTicketId(Long ticketId) {
        return solutionRepository.findByTicketId(ticketId);
    }
} 