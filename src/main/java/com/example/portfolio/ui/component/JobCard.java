package com.example.portfolio.ui.component;

import com.example.portfolio.domain.SelectionStatus;
import com.example.portfolio.service.dto.JobApplicationDto;
import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class JobCard extends VerticalLayout {

    public JobCard(JobApplicationDto job) {
        // 基本スタイル (枠線や影はそのまま)
        addClassNames(LumoUtility.BoxShadow.SMALL, LumoUtility.BorderRadius.MEDIUM);
        setPadding(true);
        setSpacing(false);
        setWidthFull();

        // ★ここを変更: ステータスに応じた「背景色」と「枠線の色」を設定
        applyStatusTheme(job.getStatus());

        setId(String.valueOf(job.getId()));

        // 企業名
        H3 companyName = new H3(job.getCompanyName());
        companyName.getStyle().set("margin", "0 0 0.5rem 0");
        companyName.getStyle().set("font-size", "1rem");

        Div badgeContainer = new Div();
        badgeContainer.getStyle().set("display", "flex").set("gap", "5px").set("flex-wrap", "wrap");

        // 志望度バッジ
        int priority = job.getPriority() != null ? job.getPriority() : 0;
        Span priorityBadge = createBadge("志望度: " + "★".repeat(priority), "contrast");
        priorityBadge.getStyle().set("color", "#d4a017"); // ゴールド
        priorityBadge.getStyle().set("background-color", "rgba(255, 255, 255, 0.5)"); // 背景を少し白くして読みやすく
        badgeContainer.add(priorityBadge);

        // 日付バッジ
        if (job.getNextActionDate() != null) {
            Span dateBadge = createBadge("📅 " + job.getNextActionDate().toString(), "contrast");
            dateBadge.getStyle().set("background-color", "rgba(255, 255, 255, 0.5)");
            badgeContainer.add(dateBadge);
        }

        // メモがあれば表示
        if (job.getMemo() != null && !job.getMemo().isEmpty()) {
            Span memoSpan = new Span(job.getMemo());
            memoSpan.getStyle().set("font-size", "0.8rem").set("color", "var(--lumo-body-text-color)");
            memoSpan.getStyle().set("margin-top", "0.5rem");
            add(companyName, badgeContainer, memoSpan);
        } else {
            add(companyName, badgeContainer);
        }

        DragSource.create(this);
    }

    private Span createBadge(String text, String theme) {
        Span badge = new Span(text);
        badge.getElement().getThemeList().add("badge " + theme);
        badge.getStyle().set("font-size", "0.75rem");
        return badge;
    }

    // ★ステータスごとの色を適用するメソッド
    private void applyStatusTheme(SelectionStatus status) {
        if (status == null) {
            getStyle().set("background-color", "var(--lumo-base-color)");
            return;
        }

        String backgroundColor;
        String borderColor;

        switch (status) {
            case OFFER: // 内定: 緑
                // 10pct は「10%の薄さ」という意味のVaadin標準変数です
                backgroundColor = "var(--lumo-success-color-10pct)";
                borderColor = "var(--lumo-success-color)";
                break;
            case REJECTED: // 不採用: 赤
                backgroundColor = "var(--lumo-error-color-10pct)";
                borderColor = "var(--lumo-error-color)";
                break;
            case FIRST_INTERVIEW: // 面接: 青
            case SECOND_INTERVIEW:
            case FINAL_INTERVIEW:
                backgroundColor = "var(--lumo-primary-color-10pct)";
                borderColor = "var(--lumo-primary-color)";
                break;
            default: // 書類選考など: グレー（標準）
                backgroundColor = "var(--lumo-contrast-5pct)";
                borderColor = "var(--lumo-contrast-50pct)";
                break;
        }

        // 背景色をセット
        getStyle().set("background-color", backgroundColor);

        // 左側の太い線もセット（アクセントとして残しておくと綺麗です）
        getStyle().set("border-left", "5px solid " + borderColor);

        // 全体の枠線もうっすら色付けたい場合は以下を有効化してください
        // getStyle().set("border", "1px solid " + borderColor);
    }
}