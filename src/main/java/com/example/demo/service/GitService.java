package com.example.demo.service;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@Service
public class GitService {
    
    @Value("${git.repository-path}")
    private String repositoryPath;
    
    @Value("${git.token}")
    private String gitToken;
    
    @Value("${pipeline.github.api-url}")
    private String githubApiUrl;
    
    @Value("${pipeline.github.owner}")
    private String repoOwner;
    
    @Value("${pipeline.github.repo}")
    private String repoName;
    
    private final WebClient webClient;
    
    public GitService() {
        this.webClient = WebClient.builder().build();
    }
    
    public String createBranchWithSolution(Long ticketId, String solutionCode, String description) {
        try {
            String branchName = "solution/ticket-" + ticketId + "-" + System.currentTimeMillis();
            
            // Open existing repository
            Git git = Git.open(new File(repositoryPath));
            
            // Store the current branch name before switching
            String currentBranch = git.getRepository().getBranch();
            
            // Create orphan branch (with no parent commit)
            ProcessBuilder processBuilder = new ProcessBuilder(
                "git", "-C", repositoryPath, "checkout", "--orphan", branchName
            );
            Process process = processBuilder.start();
            process.waitFor();
            
            // Clean the working directory in the new branch
            ProcessBuilder cleanBuilder = new ProcessBuilder(
                "git", "-C", repositoryPath, "rm", "-rf", "."
            );
            Process cleanProcess = cleanBuilder.start();
            cleanProcess.waitFor();
            
            // Create solution file
            String fileName = "Ticket" + ticketId + "Solution.java";
            Path solutionPath = Paths.get(repositoryPath, "src/main/java/solutions", fileName);
            
            // Create directories if they don't exist
            Files.createDirectories(solutionPath.getParent());
            
            // Write solution code to file
            String fullSolutionCode = generateSolutionTemplate(ticketId, solutionCode, description);
            Files.write(solutionPath, fullSolutionCode.getBytes());
            
            // Add file to git
            git.add().addFilepattern("src/main/java/solutions/" + fileName).call();
            
            // Commit changes
            git.commit()
                .setMessage("Add solution for ticket #" + ticketId + ": " + description)
                .call();
            
            // Push branch to remote
            git.push()
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider("token", gitToken))
                .setRemote("origin")
                .setForce(true)
                .add(branchName)
                .call();
            
            // Return to the original branch
            git.checkout().setName(currentBranch).call();
            
            git.close();
            
            System.out.println("✅ Branch created successfully: " + branchName);
            return branchName;
            
        } catch (Exception e) {
            System.err.println("❌ Error creating branch: " + e.getMessage());
            throw new RuntimeException("Failed to create branch", e);
        }
    }
    
    public void triggerPipeline(String branchName) {
        try {
            // Create workflow dispatch event
            String url = String.format("%s/repos/%s/%s/actions/workflows/solution-pipeline.yml/dispatches", 
                githubApiUrl, repoOwner, repoName);
            
            String requestBody = String.format("""
                {
                    "ref": "%s",
                    "inputs": {
                        "branch_name": "%s"
                    }
                }
                """, branchName, branchName);
            
            webClient.post()
                .uri(url)
                .header("Authorization", "token " + gitToken)
                .header("Accept", "application/vnd.github.v3+json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
            
            System.out.println("✅ Pipeline triggered for branch: " + branchName);
            
        } catch (Exception e) {
            System.err.println("❌ Error triggering pipeline: " + e.getMessage());
            throw new RuntimeException("Failed to trigger pipeline", e);
        }
    }
    
    private String generateSolutionTemplate(Long ticketId, String solutionCode, String description) {
        return String.format("""
            package solutions;
            
            /**
             * Solution for Ticket #%d
             * Description: %s
             * Generated on: %s
             */
            public class Ticket%dSolution {
                
                /**
                 * %s
                 */
                %s
            }
            """, 
            ticketId, 
            description, 
            java.time.LocalDateTime.now(),
            ticketId,
            description,
            solutionCode
        );
    }
} 