package com.example.portfolio.ui;

import com.example.portfolio.service.JobApplicationService;
import com.example.portfolio.service.dto.JobApplicationDto; // ★ここがDTOに変わりました
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@PageTitle("カレンダー")
@Route(value = "calendar", layout = MainLayout.class)
public class CalendarView extends VerticalLayout {

    private final JobApplicationService service;
    private YearMonth currentMonth;
    private final Div calendarGrid;
    private final H2 monthLabel;

    public CalendarView(JobApplicationService service) {
        this.service = service;
        this.currentMonth = YearMonth.now();

        setSizeFull();
        setPadding(true);

        // 1. ヘッダー（月移動ボタン）
        Button prevBtn = new Button(VaadinIcon.ARROW_LEFT.create(), e -> changeMonth(-1));
        Button nextBtn = new Button(VaadinIcon.ARROW_RIGHT.create(), e -> changeMonth(1));
        monthLabel = new H2();

        HorizontalLayout header = new HorizontalLayout(prevBtn, monthLabel, nextBtn);
        header.setAlignItems(Alignment.CENTER);

        // 2. 曜日ヘッダー
        HorizontalLayout weekHeader = new HorizontalLayout();
        weekHeader.setWidthFull();
        String[] days = {"月", "火", "水", "木", "金", "土", "日"};
        for (String day : days) {
            Div dayLabel = new Div(new Span(day));
            dayLabel.setWidthFull();
            dayLabel.getStyle().set("text-align", "center").set("font-weight", "bold");
            weekHeader.add(dayLabel);
        }

        // 3. カレンダーグリッド本体
        calendarGrid = new Div();
        calendarGrid.setSizeFull();
        calendarGrid.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "repeat(7, 1fr)")
                .set("grid-template-rows", "repeat(5, 1fr)")
                .set("gap", "10px");

        add(header, weekHeader, calendarGrid);
        refreshCalendar();
    }

    private void changeMonth(int monthsToAdd) {
        currentMonth = currentMonth.plusMonths(monthsToAdd);
        refreshCalendar();
    }

    private void refreshCalendar() {
        calendarGrid.removeAll();
        monthLabel.setText(currentMonth.format(DateTimeFormatter.ofPattern("yyyy年 MM月")));

        // ★修正点: Serviceから返ってくるのはDTOのリストです
        List<JobApplicationDto> allJobs = service.findAll();

        // ★修正点: Mapの型も DTO に変更
        Map<LocalDate, List<JobApplicationDto>> jobsByDate = allJobs.stream()
                .filter(job -> job.getNextActionDate() != null)
                .collect(Collectors.groupingBy(JobApplicationDto::getNextActionDate));

        LocalDate firstDayOfMonth = currentMonth.atDay(1);
        int daysInMonth = currentMonth.lengthOfMonth();
        int dayOfWeekOffset = firstDayOfMonth.getDayOfWeek().getValue() - 1;

        // 空白マス
        for (int i = 0; i < dayOfWeekOffset; i++) {
            calendarGrid.add(createDayCell(null, null));
        }

        // 日付マス
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = currentMonth.atDay(day);
            calendarGrid.add(createDayCell(date, jobsByDate.get(date)));
        }
    }

    // ★修正点: 引数も DTO に変更
    private Div createDayCell(LocalDate date, List<JobApplicationDto> jobs) {
        Div cell = new Div();
        cell.addClassNames(LumoUtility.Border.ALL, LumoUtility.BorderRadius.SMALL, LumoUtility.Padding.SMALL);
        cell.getStyle().set("overflow", "hidden").set("min-height", "80px");

        if (date == null) {
            cell.getStyle().set("background-color", "#f5f5f5");
            return cell;
        }

        Span dateText = new Span(String.valueOf(date.getDayOfMonth()));
        dateText.getStyle().set("font-weight", "bold");
        if (date.equals(LocalDate.now())) {
            dateText.getStyle().set("color", "red");
        }
        cell.add(dateText);

        if (jobs != null) {
            // ★修正点: ループ変数も DTO
            for (JobApplicationDto job : jobs) {
                String labelText = job.getCompanyName();
                if (job.getMemo() != null && !job.getMemo().isEmpty()) {
                    labelText += " (" + job.getMemo() + ")";
                }

                Div badge = new Div(new Span(labelText));
                badge.getStyle()
                        .set("background-color", "var(--lumo-primary-color)")
                        .set("color", "white")
                        .set("font-size", "11px")
                        .set("padding", "2px 4px")
                        .set("border-radius", "4px")
                        .set("margin-top", "2px")
                        .set("white-space", "nowrap")
                        .set("overflow", "hidden")
                        .set("text-overflow", "ellipsis");
                cell.add(badge);
            }
        }
        return cell;
    }
}