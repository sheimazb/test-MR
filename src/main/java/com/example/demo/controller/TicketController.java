package com.example.demo.controller;

import com.example.demo.entity.Ticket;
import com.example.demo.entity.TicketStatus;
import com.example.demo.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "*")
public class TicketController {
    
    @Autowired
    private TicketService ticketService;
    
    @GetMapping
    public List<Ticket> getAllTickets() {
        return ticketService.getAllTickets();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicket(@PathVariable Long id) {
        return ticketService.getTicketById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<Ticket> updateTicketStatus(
            @PathVariable Long id, 
            @RequestBody Map<String, String> request) {
        
        try {
            String statusString = request.get("status");
            TicketStatus status = TicketStatus.valueOf(statusString.toUpperCase());
            
            Ticket updatedTicket = ticketService.updateTicketStatus(id, status);
            
            return ResponseEntity.ok(updatedTicket);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    // Webhook endpoint for pipeline status updates
    @PostMapping("/pipeline-status")
    public ResponseEntity<String> updatePipelineStatus(@RequestBody Map<String, String> request) {
        try {
            String branchName = request.get("branch_name");
            String status = request.get("status");
            
            ticketService.updatePipelineStatus(branchName, status);
            
            return ResponseEntity.ok("Status updated successfully");
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating status: " + e.getMessage());
        }
    }
} 