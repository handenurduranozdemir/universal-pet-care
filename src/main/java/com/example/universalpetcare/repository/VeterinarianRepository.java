package com.example.universalpetcare.repository;

import com.example.universalpetcare.model.Veterinarian;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VeterinarianRepository extends JpaRepository<Veterinarian, Long> {
}
