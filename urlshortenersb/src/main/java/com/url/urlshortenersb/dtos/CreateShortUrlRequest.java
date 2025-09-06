package com.url.urlshortenersb.dtos;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

@Data
public class CreateShortUrlRequest {
    @NotBlank(message = "Original URL is required")
    private String originalUrl;
    
    // NEW FEATURE: expiry date - optional field for expiry in days
    @Min(value = 1, message = "Expiry days must be at least 1")
    @Max(value = 365, message = "Expiry days cannot exceed 365")
    private Integer expiryInDays;
}

