package com.example.demo.controller;

import com.example.demo.entity.Solution;
import com.example.demo.service.SolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/solutions")
@CrossOrigin(origins = "*")
public class SolutionController {
    
    @Autowired
    private SolutionService solutionService;
    
    @PostMapping("/tickets/{ticketId}")
    public ResponseEntity<Solution> createSolution(
            @PathVariable Long ticketId,
            @RequestBody Map<String, String> request) {
        
        try {
            String solutionCode = request.get("solutionCode");
            String description = request.get("description");
            
            if (solutionCode == null || solutionCode.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            Solution solution = solutionService.createSolution(ticketId, solutionCode, description);
            
            return ResponseEntity.ok(solution);
            
        } catch (RuntimeException e) {
            System.err.println("Error creating solution: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/tickets/{ticketId}")
    public List<Solution> getSolutionsByTicket(@PathVariable Long ticketId) {
        return solutionService.getSolutionsByTicketId(ticketId);
    }
} 