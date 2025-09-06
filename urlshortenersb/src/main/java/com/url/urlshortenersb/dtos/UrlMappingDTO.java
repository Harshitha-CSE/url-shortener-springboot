package com.url.urlshortenersb.dtos;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UrlMappingDTO {
    private Long id;
    private String originalUrl;
    private String shortUrl;
    private int clickCount;
    private LocalDateTime createdDate;
    private String username;
    //new feature added
    private LocalDateTime expiryDate;
    // REDIRECT FEATURE - full short URL with domain for frontend display
    private String fullShortUrl;
}

