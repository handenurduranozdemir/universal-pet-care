package com.example.universalpetcare.repository;

import com.example.universalpetcare.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
