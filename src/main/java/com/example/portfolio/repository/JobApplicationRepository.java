package com.example.portfolio.repository;

import com.example.portfolio.domain.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
}