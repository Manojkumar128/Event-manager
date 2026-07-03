package com.janavani.backend.repository;

import com.janavani.backend.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findByUserEmailIgnoreCaseAndActiveTrue(String userEmail);
    List<Registration> findByEventIdAndActiveTrue(Long eventId);
    long countByEventIdAndActiveTrue(Long eventId);
    boolean existsByEventIdAndUserEmailIgnoreCaseAndActiveTrue(Long eventId, String userEmail);
}
