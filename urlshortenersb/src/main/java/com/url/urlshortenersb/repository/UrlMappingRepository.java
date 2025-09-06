package com.url.urlshortenersb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.url.urlshortenersb.models.UrlMapping;

import java.time.LocalDateTime;
import java.util.List;
import com.url.urlshortenersb.models.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {
    UrlMapping findByShortUrl(String shortUrl);
    List<UrlMapping> findByUser(User user);

    //new feature added

     // ✅ New method: fetch short URL only if it's not expired
     UrlMapping findByShortUrlAndExpiryDateAfter(String shortUrl, LocalDateTime now);

     List<UrlMapping> findByUserAndExpiryDateAfter(User user, LocalDateTime now);
     
     // NEW FEATURE: expiry date - find expired URLs for cleanup
     List<UrlMapping> findByExpiryDateBefore(LocalDateTime now);
}
