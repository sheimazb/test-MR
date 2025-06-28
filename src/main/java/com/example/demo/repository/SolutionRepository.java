package com.example.demo.repository;

import com.example.demo.entity.Solution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SolutionRepository extends JpaRepository<Solution, Long> {
    List<Solution> findByTicketId(Long ticketId);
    Solution findByBranchName(String branchName);
} 