package com.example.portfolio.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity // これをつけるとDBのテーブルになる
public class JobApplication {

    @Id // 主キー (Primary Key)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自動採番 (Auto Increment)
    private Long id;

    private String companyName;

    @Enumerated(EnumType.STRING) // Enumを文字列としてDBに保存する
    private SelectionStatus status;

    private Integer priority; // 志望度 (1-5)

    private LocalDate nextActionDate; // 次回アクション日

    private String memo;

    public JobApplication() {
        // JPAのために空のコンストラクタが必須
    }

    public JobApplication(String companyName, SelectionStatus status, Integer priority) {
        this.companyName = companyName;
        this.status = status;
        this.priority = priority;
        this.nextActionDate = LocalDate.now(); // とりあえず今日を入れる
    }

    // --- ゲッターとセッター ---
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
