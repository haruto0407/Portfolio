package com.example.portfolio.service.dto;

import com.example.portfolio.domain.SelectionStatus;
import java.time.LocalDate;

// 画面とServiceの間でデータを受け渡しするためだけのクラス
public class JobApplicationDto {

    private Long id;
    private String companyName;
    private SelectionStatus status;
    private Integer priority;
    private LocalDate nextActionDate;
    private String memo;

    // コンストラクタ（全フィールド初期化用）
    public JobApplicationDto(Long id, String companyName, SelectionStatus status, Integer priority, LocalDate nextActionDate, String memo) {
        this.id = id;
        this.companyName = companyName;
        this.status = status;
        this.priority = priority;
        this.nextActionDate = nextActionDate;
        this.memo = memo;
    }

    // 空のコンストラクタ（Binderなどで必要）
    public JobApplicationDto() {
    }

    // ゲッター・セッター
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public SelectionStatus getStatus() { return status; }
    public void setStatus(SelectionStatus status) { this.status = status; }

    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }

    public LocalDate getNextActionDate() { return nextActionDate; }
    public void setNextActionDate(LocalDate nextActionDate) { this.nextActionDate = nextActionDate; }

    public String getMemo() { return memo; }
    public void setMemo(String memo) { this.memo = memo; }
}