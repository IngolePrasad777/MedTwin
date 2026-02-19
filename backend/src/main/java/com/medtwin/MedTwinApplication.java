package com.medtwin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class MedTwinApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(MedTwinApplication.class, args);
        System.out.println("\n" +
            "╔═══════════════════════════════════════════════════════════╗\n" +
            "║                                                           ║\n" +
            "║   MedTwin Backend - Medical Device Digital Twin Engine   ║\n" +
            "║                                                           ║\n" +
            "║   Server running on: http://localhost:8080               ║\n" +
            "║   H2 Console: http://localhost:8080/h2-console           ║\n" +
            "║   API Docs: http://localhost:8080/api/health             ║\n" +
            "║                                                           ║\n" +
            "╚═══════════════════════════════════════════════════════════╝\n"
        );
    }
}
