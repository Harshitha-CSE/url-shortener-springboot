package com.url.urlshortenersb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // NEW FEATURE: expiry date - enable scheduling for cleanup tasks
public class UrlshortenersbApplication {

	public static void main(String[] args) {
		SpringApplication.run(UrlshortenersbApplication.class, args);
	}

}
