package com.example.portfolio.service;

import com.example.portfolio.service.dto.JobApplicationDto;
import com.example.portfolio.domain.SelectionStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class JobAdvisorService {

    // 全体の状況を見て、総評を行うメソッド
    public String getGeneralAdvice(List<JobApplicationDto> jobs) {
        long offers = jobs.stream().filter(j -> j.getStatus() == SelectionStatus.OFFER).count();
        if (offers > 0) {
            return "🎉 内定おめでとうございます！承諾期限や条件の確認を優先しましょう。";
        }

        long interviewCount = jobs.stream()
                .filter(j -> j.getStatus() == SelectionStatus.FIRST_INTERVIEW ||
                        j.getStatus() == SelectionStatus.SECOND_INTERVIEW ||
                        j.getStatus() == SelectionStatus.FINAL_INTERVIEW)
                .count();

        if (interviewCount > 3) {
            return "🔥 面接ラッシュですね！スケジュール管理と体調管理を最優先に。";
        }

        if (jobs.isEmpty()) {
            return "まずは「企業を追加」ボタンから、気になる企業を登録してみましょう！";
        }

        return "コツコツ進めていきましょう。志望度が高い企業の企業研究は進んでいますか？";
    }
}