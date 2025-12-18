package com.example.portfolio.ui;

import com.example.portfolio.domain.SelectionStatus;
import com.example.portfolio.service.JobApplicationService;
import com.example.portfolio.service.dto.JobApplicationDto;
import com.example.portfolio.ui.component.DashboardStats;
import com.example.portfolio.ui.component.JobDialog;
import com.example.portfolio.ui.component.KanbanColumn;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.List;
import java.util.stream.Collectors;

@PageTitle("進捗ボード")
@Route(value = "", layout = MainLayout.class)
public class MainView extends VerticalLayout {

    private final JobApplicationService service;

    private final DashboardStats statsBoard = new DashboardStats();
    private final HorizontalLayout kanbanLayout = new HorizontalLayout();

    // ★追加: 検索ボックスをフィールドとして持っておく（リフレッシュ時に値を取得するため）
    private final TextField searchField = new TextField();

    public MainView(JobApplicationService service) {
        this.service = service;

        setSizeFull();
        setPadding(true);
        setSpacing(true);
        addClassName(LumoUtility.Background.CONTRAST_5);

        // レイアウト構築
        add(createHeader(), statsBoard, kanbanLayout);

        kanbanLayout.setSizeFull();
        kanbanLayout.getStyle().set("overflow-x", "auto");
        kanbanLayout.setAlignItems(Alignment.STRETCH);
        expand(kanbanLayout);

        refreshBoard();
    }

    private Component createHeader() {
        H2 title = new H2("Dashboard");
        title.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.Margin.NONE);

        // ★追加: 検索ボックスの設定
        searchField.setPlaceholder("企業名で検索...");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        // 入力するたびに即座にイベントを発火させすぎないよう "LAZY" (少し遅延) に設定
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.addValueChangeListener(e -> refreshBoard()); // 値が変わったら再描画
        searchField.setWidth("300px");

        Button addButton = new Button("New", new Icon(VaadinIcon.PLUS), e -> openJobDialog(new JobApplicationDto()));
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // ★変更: 検索ボックスをレイアウトに追加
        HorizontalLayout topBar = new HorizontalLayout(title, searchField, addButton);
        topBar.setWidthFull();
        // searchFieldを真ん中寄せにするなどの調整はお好みで
        topBar.setJustifyContentMode(JustifyContentMode.BETWEEN);
        topBar.setAlignItems(Alignment.CENTER);
        return topBar;
    }

    private void refreshBoard() {
        String filterText = searchField.getValue();

        // ★修正: UIでfilterせず、Serviceに検索させる
        // これにより、DB側で絞り込まれた軽いデータだけが返ってくる
        List<JobApplicationDto> filteredApplications = service.findByCompanyName(filterText);

        // 統計などの更新
        statsBoard.update(filteredApplications);

        kanbanLayout.removeAll();
        for (SelectionStatus status : SelectionStatus.values()) {
            KanbanColumn column = new KanbanColumn(
                    status,
                    this::openJobDialog,
                    this::handleDrop
            );
            column.setJobs(filteredApplications);
            kanbanLayout.add(column);
        }
    }

    private void handleDrop(Long id, SelectionStatus newStatus) {
        service.updateStatus(id, newStatus);
        refreshBoard();
    }

    private void openJobDialog(JobApplicationDto job) {
        JobDialog dialog = new JobDialog(
                job,
                (savedJob) -> {
                    if (savedJob.getStatus() == null) savedJob.setStatus(SelectionStatus.DOCUMENT_SCREENING);
                    service.save(savedJob);
                    refreshBoard();
                    Notification.show("Saved successfully");
                },
                (deletedJob) -> {
                    service.delete(deletedJob);
                    refreshBoard();
                    Notification.show("Deleted successfully");
                }
        );
        dialog.open();
    }
}