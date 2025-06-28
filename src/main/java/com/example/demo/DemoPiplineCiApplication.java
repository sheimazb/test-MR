package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class DemoPiplineCiApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(DemoPiplineCiApplication.class);
		Environment env = app.run(args).getEnvironment();
		
		System.out.println("✅ Ticket Solution Pipeline Application Started");
		System.out.println("✅ Running with profile: " + String.join(", ", env.getActiveProfiles()));
		System.out.println("✅ Access URL: http://localhost:" + env.getProperty("server.port") + "/static/index.html");
	}

}
