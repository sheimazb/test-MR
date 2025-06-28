# Ticket Solution Pipeline

A Spring Boot application that implements a ticket system with CI/CD pipeline integration for managing solutions.

## Setup Instructions

### 1. Database Setup

1. Create a MySQL database:
   ```sql
   CREATE DATABASE ticket_system;
   ```

2. Configure database credentials in `src/main/resources/application-local.properties`:
   ```properties
   spring.datasource.username=your_mysql_username
   spring.datasource.password=your_mysql_password
   ```

### 2. Git Configuration

1. Update the Git configuration in `src/main/resources/application-local.properties`:
   ```properties
   git.repository-path=C:/path/to/your/repository
   git.remote-url=https://github.com/yourusername/your-repo.git
   git.token=your_github_token
   ```

2. Create a GitHub personal access token:
   - Go to GitHub → Settings → Developer settings → Personal access tokens → Tokens (classic)
   - Generate a new token with the following permissions:
     - `repo` (Full control of private repositories)
     - `workflow` (Update GitHub Action workflows)
   - Copy the token and update in your local properties file
   - **IMPORTANT**: Never commit your token to Git!

### 3. Application Configuration

1. Create `application-local.properties` from the template:
   ```bash
   cp src/main/resources/application-local.properties.template src/main/resources/application-local.properties
   ```

2. Edit the file with your actual values

3. Run the application with the local profile:
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=local
   ```

### 4. GitHub Actions Setup

1. Push your code to GitHub (without sensitive information):
   ```bash
   git add .
   git commit -m "Initial commit"
   git push -u origin main
   ```

2. Add GitHub Secrets for your repository:
   - Go to your GitHub repository → Settings → Secrets and variables → Actions
   - Add a new repository secret:
     - Name: `SPRING_APP_URL`
     - Value: `http://your-server-domain:8080` (or your actual server URL)

### 5. Running the Application

1. Start the Spring Boot application:
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=local
   ```

2. Access the frontend interface:
   ```
   http://localhost:8080/static/index.html
   ```

## Testing the Workflow

1. Create a ticket (or use the sample ticket)
2. Submit a solution for the ticket
3. Change the ticket status to MERGED to trigger the pipeline
4. Monitor the pipeline execution on GitHub Actions
5. Verify that the ticket status changes to TEST after successful pipeline execution
