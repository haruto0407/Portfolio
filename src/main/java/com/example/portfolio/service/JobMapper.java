package com.example.portfolio.service;

import com.example.portfolio.domain.JobApplication;
import com.example.portfolio.service.dto.JobApplicationDto;
import org.springframework.stereotype.Component;

@Component
public class JobMapper {

    // Entity -> DTO (DBから画面へ)
    public JobApplicationDto toDto(JobApplication entity) {
        if (entity == null) return null;
        return new JobApplicationDto(
                entity.getId(),
                entity.getCompanyName(),
                entity.getStatus(),
                entity.getPriority(),
                entity.getNextActionDate(),
                entity.getMemo()
        );
    }

    // DTO -> Entity (画面からDBへ: 新規作成)
    public JobApplication toEntity(JobApplicationDto dto) {
        JobApplication entity = new JobApplication();
        return updateEntity(dto, entity);
    }

    // DTO -> Entity (画面からDBへ: 更新)
    public JobApplication updateEntity(JobApplicationDto dto, JobApplication entity) {
        entity.setCompanyName(dto.getCompanyName());
        entity.setStatus(dto.getStatus());
        entity.setPriority(dto.getPriority());
        entity.setNextActionDate(dto.getNextActionDate());
        entity.setMemo(dto.getMemo());
        // IDは更新しない
        return entity;
    }
}