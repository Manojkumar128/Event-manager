package com.janavani.backend;

import com.janavani.backend.model.Event;
import com.janavani.backend.repository.EventRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Bean
	public CommandLineRunner dataInitializer(EventRepository eventRepository) {
		return args -> {
			if (eventRepository.count() == 0) {
				eventRepository.save(new Event(
					"Spring Boot Workshop",
					"Learn Spring Boot and build a Java event registration backend.",
					"Online",
					LocalDateTime.now().plusDays(7),
					LocalDateTime.now().plusDays(7).plusHours(3),
					50
				));
				eventRepository.save(new Event(
					"Community Networking Meetup",
					"Join other event organizers and attendees for networking.",
					"City Hall",
					LocalDateTime.now().plusDays(14),
					LocalDateTime.now().plusDays(14).plusHours(2),
					100
				));
			}
		};
	}
}
