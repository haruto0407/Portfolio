package com.example.portfolio.ui.component;

import com.example.portfolio.domain.SelectionStatus;
import com.example.portfolio.service.dto.JobApplicationDto;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.List;

public class DashboardStats extends HorizontalLayout {

    public DashboardStats() {
        setWidthFull();
        addClassNames(LumoUtility.Margin.Bottom.MEDIUM);
    }

    // データを渡すと、計算して表示を更新する
    public void update(List<JobApplicationDto> jobs) {
        removeAll();

        long total = jobs.size();
        long active = jobs.stream().filter(j ->
                j.getStatus() == SelectionStatus.FIRST_INTERVIEW ||
                        j.getStatus() == SelectionStatus.SECOND_INTERVIEW ||
                        j.getStatus() == SelectionStatus.FINAL_INTERVIEW).count();
        long offers = jobs.stream().filter(j -> j.getStatus() == SelectionStatus.OFFER).count();

        add(createCard("全企業数", String.valueOf(total), VaadinIcon.ARCHIVE));
        add(createCard("選考中企業", String.valueOf(active), VaadinIcon.USER));
        add(createCard("内定済企業", String.valueOf(offers), VaadinIcon.TROPHY));
    }

    private Component createCard(String label, String value, VaadinIcon icon) {
        VerticalLayout card = new VerticalLayout();
        card.addClassNames(LumoUtility.Background.BASE, LumoUtility.BoxShadow.SMALL, LumoUtility.BorderRadius.MEDIUM);
        card.setPadding(true);
        card.setSpacing(false);
        card.setWidth("200px");

        Icon i = icon.create();
        i.addClassName(LumoUtility.TextColor.PRIMARY);
        i.setSize("24px");

        H2 val = new H2(value);
        val.addClassNames(LumoUtility.Margin.NONE, LumoUtility.FontSize.XXLARGE);

        Span lbl = new Span(label);
        lbl.addClassNames(LumoUtility.TextColor.SECONDARY, LumoUtility.FontSize.SMALL);

        card.add(new HorizontalLayout(i, lbl), val);
        return card;
    }
}