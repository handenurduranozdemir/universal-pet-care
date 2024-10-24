package com.example.universalpetcare.repository;

import com.example.universalpetcare.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
