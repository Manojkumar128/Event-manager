package com.janavani.backend.controller;

import com.janavani.backend.model.Event;
import com.janavani.backend.model.Registration;
import com.janavani.backend.repository.EventRepository;
import com.janavani.backend.repository.RegistrationRepository;
import com.janavani.backend.dto.RegistrationRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class EventController {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;

    public EventController(EventRepository eventRepository, RegistrationRepository registrationRepository) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
    }

    @GetMapping("/events")
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return eventRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/events")
    public ResponseEntity<Event> createEvent(@Valid @RequestBody Event event) {
        if (event.getStartDateTime() == null) {
            return ResponseEntity.badRequest().build();
        }
        Event saved = eventRepository.save(event);
        return ResponseEntity.created(URI.create("/api/events/" + saved.getId())).body(saved);
    }

    @PostMapping("/registrations")
    public ResponseEntity<?> registerForEvent(@Valid @RequestBody RegistrationRequest request) {
        return eventRepository.findById(request.getEventId()).map(event -> {
            if (!event.getCapacity().equals(0) && registrationRepository.countByEventIdAndActiveTrue(event.getId()) >= event.getCapacity()) {
                return ResponseEntity.badRequest().body("Event is full");
            }
            if (registrationRepository.existsByEventIdAndUserEmailIgnoreCaseAndActiveTrue(event.getId(), request.getUserEmail())) {
                return ResponseEntity.badRequest().body("Already registered for this event");
            }
            Registration registration = new Registration();
            registration.setEvent(event);
            registration.setUserName(request.getUserName());
            registration.setUserEmail(request.getUserEmail());
            registration.setRegisteredAt(LocalDateTime.now());
            Registration saved = registrationRepository.save(registration);
            return ResponseEntity.created(URI.create("/api/registrations/" + saved.getId())).body(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/registrations")
    public List<Registration> getRegistrations(@RequestParam(required = false) String email,
                                               @RequestParam(required = false) Long eventId) {
        if (email != null) {
            return registrationRepository.findByUserEmailIgnoreCaseAndActiveTrue(email);
        }
        if (eventId != null) {
            return registrationRepository.findByEventIdAndActiveTrue(eventId);
        }
        return registrationRepository.findAll();
    }

    @DeleteMapping("/registrations/{id}")
    public ResponseEntity<?> cancelRegistration(@PathVariable Long id) {
        return registrationRepository.findById(id).map(registration -> {
            registration.setActive(false);
            registrationRepository.save(registration);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
