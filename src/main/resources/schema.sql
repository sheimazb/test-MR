-- Tickets table with status workflow
CREATE TABLE IF NOT EXISTS tickets (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status ENUM('IN_PROGRESS', 'RESOLVED', 'MERGED', 'TEST', 'CLOSED') DEFAULT 'IN_PROGRESS',
    solution_branch VARCHAR(255),
    pipeline_status VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Solutions table
CREATE TABLE IF NOT EXISTS solutions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    ticket_id BIGINT NOT NULL,
    solution_code TEXT NOT NULL,
    description TEXT,
    file_path VARCHAR(500),
    branch_name VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ticket_id) REFERENCES tickets(id) ON DELETE CASCADE
);

-- Insert sample ticket for testing (if no tickets exist)
INSERT INTO tickets (title, description, status) 
SELECT 'Fix login bug', 'Users cannot login with special characters', 'IN_PROGRESS'
WHERE NOT EXISTS (SELECT 1 FROM tickets LIMIT 1); 