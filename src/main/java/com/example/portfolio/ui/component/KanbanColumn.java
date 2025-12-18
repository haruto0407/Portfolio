package com.example.portfolio.ui.component;

import com.example.portfolio.domain.SelectionStatus;
import com.example.portfolio.service.dto.JobApplicationDto;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class KanbanColumn extends VerticalLayout {

    private final SelectionStatus status;
    private final VerticalLayout cardArea;
    private final Consumer<JobApplicationDto> onCardClick;
    private final BiConsumer<Long, SelectionStatus> onDrop; // IDと新ステータスを親に伝える

    public KanbanColumn(SelectionStatus status, Consumer<JobApplicationDto> onCardClick, BiConsumer<Long, SelectionStatus> onDrop) {
        this.status = status;
        this.onCardClick = onCardClick;
        this.onDrop = onDrop;

        setMinWidth("320px");
        setMaxWidth("360px");
        setHeightFull();
        addClassNames(LumoUtility.Background.BASE, LumoUtility.BorderRadius.LARGE);

        // カードエリアの初期化
        cardArea = new VerticalLayout();
        cardArea.setPadding(false);
        cardArea.setSpacing(true);
        cardArea.setSizeFull();
        cardArea.getStyle().set("overflow-y", "auto");

        // ドロップ受け入れ設定
        setupDropTarget();
    }

    // データを渡して描画するメソッド
    public void setJobs(List<JobApplicationDto> allJobs) {
        removeAll();
        cardArea.removeAll();

        // 1. このカラムのステータスに一致するものだけ抽出＆ソート
        List<JobApplicationDto> items = filterAndSort(allJobs);

        // 2. ヘッダー作成
        createHeader(items.size());

        // 3. カード作成
        for (JobApplicationDto item : items) {
            JobCard card = new JobCard(item);
            card.addClickListener(e -> onCardClick.accept(item));
            cardArea.add(card);
        }

        add(cardArea);
    }

    private List<JobApplicationDto> filterAndSort(List<JobApplicationDto> allJobs) {
        Comparator<JobApplicationDto> sortingLogic = Comparator
                .comparing((JobApplicationDto dto) -> dto.getPriority() == null ? 0 : dto.getPriority(), Comparator.reverseOrder())
                .thenComparing(JobApplicationDto::getCompanyName, Comparator.nullsLast(String::compareTo))
                .thenComparing(JobApplicationDto::getId, Comparator.reverseOrder());

        return allJobs.stream()
                .filter(item -> item.getStatus() == status)
                .sorted(sortingLogic)
                .collect(Collectors.toList());
    }

    private void createHeader(long count) {
        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidthFull();
        headerLayout.setAlignItems(Alignment.CENTER);
        headerLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);

        H4 header = new H4(status.getLabel());
        header.addClassNames(LumoUtility.Margin.NONE);

        Span countBadge = new Span(String.valueOf(count));
        countBadge.getElement().getThemeList().add("badge pill");

        headerLayout.add(header, countBadge);
        // ヘッダーを最初に追加
        addComponentAsFirst(headerLayout);
    }

    private void setupDropTarget() {
        DropTarget<VerticalLayout> dropTarget = DropTarget.create(this);
        dropTarget.setActive(true);

        dropTarget.addDropListener(event -> {
            event.getDragSourceComponent().ifPresent(component -> {
                String idStr = component.getId().orElse("");
                if (!idStr.isEmpty()) {
                    Long id = Long.parseLong(idStr);
                    // 親に「このIDがこのステータスに落ちてきたよ」と伝える
                    onDrop.accept(id, status);
                }
            });
        });
    }
}