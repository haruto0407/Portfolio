package com.example.portfolio.repository;

import com.example.portfolio.domain.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByCompanyNameContainingIgnoreCase(String keyword);
}