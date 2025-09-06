package com.url.urlshortenersb.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import com.url.urlshortenersb.dtos.*;
import com.url.urlshortenersb.models.UrlMapping;
import com.url.urlshortenersb.models.User;

import java.time.LocalDateTime;

import com.url.urlshortenersb.repository.UrlMappingRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Random;

import com.url.urlshortenersb.repository.ClickEventRepository;

import java.util.stream.Collectors;

import com.url.urlshortenersb.models.ClickEvent;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UrlMappingService {

    private final UrlMappingRepository urlMappingRepository;
    private final ClickEventRepository clickEventRepository;
    
    // REDIRECT FEATURE - backend URL for generating full short URLs
    @Value("${backend.url:http://localhost:8080}")
    private String backendUrl;

    public UrlMappingDTO createShortUrl(String originalUrl, User user) {
        return createShortUrl(originalUrl, user, 7); // Default to 7 days
    }

    // NEW FEATURE: expiry date - overloaded method to handle expiry days
    public UrlMappingDTO createShortUrl(String originalUrl, User user, Integer expiryInDays) {
        // Logic to create a short URL
        String shortUrl = generateShortUrl();
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setShortUrl(shortUrl);
        urlMapping.setUser(user);
        urlMapping.setCreatedDate(LocalDateTime.now());

        // NEW FEATURE: expiry date - set expiry date based on provided days or default to 7
        int days = (expiryInDays != null && expiryInDays > 0) ? expiryInDays : 7;
        urlMapping.setExpiryDate(LocalDateTime.now().plusDays(days));

        // Save the UrlMapping entity to the database
        UrlMapping savedUrlMapping = urlMappingRepository.save(urlMapping);
        return convertToDto(savedUrlMapping);
    }

    private UrlMappingDTO convertToDto(UrlMapping urlMapping) {
        UrlMappingDTO urlMappingDTO = new UrlMappingDTO();
        urlMappingDTO.setId(urlMapping.getId());
        urlMappingDTO.setOriginalUrl(urlMapping.getOriginalUrl());
        urlMappingDTO.setShortUrl(urlMapping.getShortUrl());
        urlMappingDTO.setClickCount(urlMapping.getClickCount());
        urlMappingDTO.setCreatedDate(urlMapping.getCreatedDate());
        urlMappingDTO.setUsername(urlMapping.getUser().getUsername());
        // NEW FEATURE: expiry date - include expiry date in DTO
        urlMappingDTO.setExpiryDate(urlMapping.getExpiryDate());
        // REDIRECT FEATURE - generate full short URL with backend domain
        urlMappingDTO.setFullShortUrl(backendUrl + "/" + urlMapping.getShortUrl());
        return urlMappingDTO;
    }

    private String generateShortUrl() {
        // Logic to generate a short URL from the original URL
        // This is a placeholder implementation
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder shortUrl = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            shortUrl.append(characters.charAt(random.nextInt(characters.length())));
        }
        return shortUrl.toString();
    }

    public List<UrlMappingDTO> getUrlsByUser(User user) {
        return urlMappingRepository.findByUser(user).stream()
        .map(this::convertToDto)
        .toList();
    }

    public List<ClickEventDTO> getClickEventsByDate(String shortUrl, LocalDateTime start, LocalDateTime end) {
        UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
            if(urlMapping!=null){
                return clickEventRepository.findByUrlMappingAndClickDateBetween(urlMapping, start, end).stream()
                .collect(Collectors.groupingBy(
                    click -> click.getClickDate().toLocalDate(),
                    Collectors.counting()
                ))
                .entrySet().stream()
                .map(entry -> {
                    ClickEventDTO clickEventDTO = new ClickEventDTO();
                    clickEventDTO.setClickDate(entry.getKey());
                    clickEventDTO.setCount(entry.getValue());
                    return clickEventDTO;
                })
                .collect(Collectors.toList());
            }
        return null;
    }

    public Map<LocalDate, Long> getTotalClicksByUserAndDate(User user, LocalDate start, LocalDate end) {
        List<UrlMapping> urlMappings = urlMappingRepository.findByUser(user);
        List<ClickEvent> clickEvents = clickEventRepository.findByUrlMappingInAndClickDateBetween(urlMappings, start.atStartOfDay(), end.plusDays(1).atStartOfDay());
        return clickEvents.stream()
            .collect(Collectors.groupingBy(
                click -> click.getClickDate().toLocalDate(), Collectors.counting()));
    }

    public UrlMapping getOriginalUrl(String shortUrl) {
        UrlMapping urlMapping= urlMappingRepository.findByShortUrl(shortUrl);
        if(urlMapping != null){
            // NEW FEATURE: expiry date - check if URL has expired
            if (urlMapping.getExpiryDate() != null && urlMapping.getExpiryDate().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("This short link has expired!");
            }

            // Increment click count
            urlMapping.setClickCount(urlMapping.getClickCount() + 1);
            urlMappingRepository.save(urlMapping);
            //record click event
            ClickEvent clickEvent = new ClickEvent();
            clickEvent.setClickDate(LocalDateTime.now());
            clickEvent.setUrlMapping(urlMapping);
            clickEventRepository.save(clickEvent);
        }
        return urlMapping;
    }

    // NEW FEATURE: expiry date - method to clean up expired URLs
    public void deleteExpiredUrls() {
        LocalDateTime now = LocalDateTime.now();
        List<UrlMapping> expiredUrls = urlMappingRepository.findByExpiryDateBefore(now);
        if (!expiredUrls.isEmpty()) {
            urlMappingRepository.deleteAll(expiredUrls);
        }
    }
}
