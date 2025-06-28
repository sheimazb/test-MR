package com.example.demo.repository;

import com.example.demo.entity.Ticket;
import com.example.demo.entity.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByStatus(TicketStatus status);
    Ticket findBySolutionBranch(String branchName);
} 