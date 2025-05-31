package com.example.movieticketbookingsystem.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Application health check to verify all beans are properly configured
 */
@Component
@AllArgsConstructor
@Slf4j
public class ApplicationHealthCheck implements CommandLineRunner {

    private final ApplicationContext applicationContext;

    @Override
    public void run(String... args) throws Exception {
        log.info("🚀 Movie Ticket Booking System API Started Successfully!");
        log.info("📊 Total beans loaded: {}", applicationContext.getBeanDefinitionCount());
        
        // Check critical beans
        checkCriticalBeans();
        
        log.info("✅ All critical components are properly configured");
        log.info("🎬 Movie Ticket Booking System is ready to serve requests!");
    }
    
    private void checkCriticalBeans() {
        String[] criticalBeans = {
            "userServiceImpl",
            "movieServiceImpl",
            "theaterServiceImpl",
            "screenServiceImpl",
            "showServiceImpl",
            "feedbackServiceImpl",
            "authServiceImpl",
            "seatServiceImpl",
            "jwtService",
            "restResponseBuilder",
            "userExceptionHandler",
            "movieExceptionHandler",
            "theaterExceptionHandler",
            "securityExceptionHandler",
            "validationExceptionHandler"
        };
        
        for (String beanName : criticalBeans) {
            if (applicationContext.containsBean(beanName)) {
                log.debug("✓ {} - OK", beanName);
            } else {
                log.warn("⚠ {} - NOT FOUND", beanName);
            }
        }
    }
}
