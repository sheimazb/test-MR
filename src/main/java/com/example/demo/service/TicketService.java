package com.example.demo.service;

import com.example.demo.entity.Ticket;
import com.example.demo.entity.TicketStatus;
import com.example.demo.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    
    @Autowired
    private TicketRepository ticketRepository;
    
    @Autowired
    private GitService gitService;
    
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }
    
    public Optional<Ticket> getTicketById(Long id) {
        return ticketRepository.findById(id);
    }
    
    public Ticket updateTicketStatus(Long ticketId, TicketStatus newStatus) {
        Ticket ticket = ticketRepository.findById(ticketId)
            .orElseThrow(() -> new RuntimeException("Ticket not found"));
        
        TicketStatus oldStatus = ticket.getStatus();
        ticket.setStatus(newStatus);
        
        // If status changed to MERGED, trigger the pipeline
        if (newStatus == TicketStatus.MERGED && oldStatus != TicketStatus.MERGED) {
            if (ticket.getSolutionBranch() != null) {
                System.out.println("🚀 Triggering pipeline for ticket #" + ticketId);
                ticket.setPipelineStatus("running");
                ticketRepository.save(ticket); // Save immediately
                
                // Trigger pipeline in background
                new Thread(() -> {
                    try {
                        gitService.triggerPipeline(ticket.getSolutionBranch());
                    } catch (Exception e) {
                        System.err.println("Pipeline trigger failed: " + e.getMessage());
                        ticket.setPipelineStatus("failed");
                        ticketRepository.save(ticket);
                    }
                }).start();
            } else {
                throw new RuntimeException("Cannot merge ticket without solution branch");
            }
        }
        
        return ticketRepository.save(ticket);
    }
    
    public void updatePipelineStatus(String branchName, String status) {
        Ticket ticket = ticketRepository.findBySolutionBranch(branchName);
        if (ticket != null) {
            ticket.setPipelineStatus(status);
            
            // If pipeline completed successfully, update ticket status to TEST
            if ("success".equals(status)) {
                ticket.setStatus(TicketStatus.TEST);
            }
            
            ticketRepository.save(ticket);
        }
    }
} 