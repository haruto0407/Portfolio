package com.example.portfolio.service;

import com.example.portfolio.domain.JobApplication;
import com.example.portfolio.domain.SelectionStatus;
import com.example.portfolio.repository.JobApplicationRepository;
import com.example.portfolio.service.dto.JobApplicationDto; // DTOをインポート
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class JobApplicationService {

    private final JobApplicationRepository repository;
    private final JobMapper mapper; // Mapperを追加

    public JobApplicationService(JobApplicationRepository repository, JobMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    // 戻り値を DTO のリストに変更
    public List<JobApplicationDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto) // 全件変換
                .collect(Collectors.toList());
    }

    // 引数を DTO に変更
    public void save(JobApplicationDto dto) {
        JobApplication entity;
        if (dto.getId() != null) {
            // 更新: IDで検索して書き換え
            entity = repository.findById(dto.getId()).orElse(new JobApplication());
            mapper.updateEntity(dto, entity);
        } else {
            // 新規: DTOからEntityを作成
            entity = mapper.toEntity(dto);
        }
        repository.save(entity);
    }

    // 引数を DTO に変更
    public void delete(JobApplicationDto dto) {
        if (dto.getId() != null) {
            repository.deleteById(dto.getId());
        }
    }

    public void updateStatus(Long id, SelectionStatus newStatus) {
        repository.findById(id).ifPresent(job -> {
            job.setStatus(newStatus);
            repository.save(job);
        });
    }
}