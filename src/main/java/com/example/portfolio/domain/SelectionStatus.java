package com.example.portfolio.domain;

public enum SelectionStatus {
    DOCUMENT_SCREENING("書類選考"),
    FIRST_INTERVIEW("一次面接"),
    SECOND_INTERVIEW("二次面接"),
    FINAL_INTERVIEW("最終面接"),
    OFFER("内定"),
    REJECTED("不採用/辞退");

    private final String label;

    SelectionStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
