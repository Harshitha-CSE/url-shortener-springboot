package com.url.urlshortenersb.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

// NEW FEATURE: expiry date - service for cleaning up expired URLs
@Service
@AllArgsConstructor
public class UrlCleanupService {
    
    private UrlMappingService urlMappingService;
    
    // NEW FEATURE: expiry date - scheduled task to clean up expired URLs every hour
    @Scheduled(fixedRate = 3600000) // Run every hour (3600000 ms)
    public void cleanupExpiredUrls() {
        try {
            urlMappingService.deleteExpiredUrls();
        } catch (Exception e) {
            // Log error but don't let it stop the scheduled task
            System.err.println("Error cleaning up expired URLs: " + e.getMessage());
        }
    }
}

