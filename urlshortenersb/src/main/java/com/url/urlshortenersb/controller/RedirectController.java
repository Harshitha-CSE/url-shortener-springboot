package com.url.urlshortenersb.controller;

import org.springframework.web.bind.annotation.RestController;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import com.url.urlshortenersb.service.UrlMappingService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import com.url.urlshortenersb.models.UrlMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;

@RestController
@AllArgsConstructor
public class RedirectController {

    private UrlMappingService urlMappingService;
    
    // REDIRECT FEATURE - GET endpoint for short URL redirection
    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortUrl) {
        try {
            // REDIRECT FEATURE - Validate short URL format
            if (shortUrl == null || shortUrl.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            // Logic to find the original URL by short URL
            UrlMapping urlMapping = urlMappingService.getOriginalUrl(shortUrl);
            
            if (urlMapping != null) {
                // REDIRECT FEATURE - Return 302 FOUND with Location header
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.add("Location", urlMapping.getOriginalUrl());
                return ResponseEntity.status(HttpStatus.FOUND)
                    .headers(httpHeaders)
                    .build();
            } else {
                // REDIRECT FEATURE - Return 404 Not Found if URL not found
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            // REDIRECT FEATURE - Handle expired URLs with 410 Gone
            if (e.getMessage().contains("expired")) {
                return ResponseEntity.status(HttpStatus.GONE).build();
            }
            // For other runtime exceptions, return 404
            return ResponseEntity.notFound().build();
        }
    }

}
