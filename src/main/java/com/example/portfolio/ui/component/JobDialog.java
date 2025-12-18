package com.example.portfolio.ui.component;

import com.example.portfolio.service.dto.JobApplicationDto; // ★DTOをインポート
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import java.util.function.Consumer;

// 入力フォームの責務を持つクラス
public class JobDialog extends Dialog {

    // ★Binderの型を DTO に変更
    private final Binder<JobApplicationDto> binder = new Binder<>(JobApplicationDto.class);

    // ★引数とConsumerの型をすべて DTO に変更
    public JobDialog(JobApplicationDto job, Consumer<JobApplicationDto> onSave, Consumer<JobApplicationDto> onDelete) {
        setHeaderTitle(job.getId() == null ? "新規登録" : "編集");

        VerticalLayout layout = new VerticalLayout();

        TextField companyNameField = new TextField("企業名");
        IntegerField priorityField = new IntegerField("志望度 (1-5)");
        priorityField.setMin(1);
        priorityField.setMax(5);
        priorityField.setStepButtonsVisible(true);

        DatePicker dateField = new DatePicker("次回アクション日");
        TextField memoField = new TextField("内容");
        memoField.setWidthFull();
        HorizontalLayout actionLayout = new HorizontalLayout(dateField, memoField);
        actionLayout.setWidthFull();

        // バリデーションとバインド
        // DTOに変えてもメソッド名（getCompanyNameなど）は同じなので、ここはそのまま動きます
        binder.forField(companyNameField).asRequired("必須です").bind(JobApplicationDto::getCompanyName, JobApplicationDto::setCompanyName);
        binder.bind(priorityField, JobApplicationDto::getPriority, JobApplicationDto::setPriority);
        binder.bind(dateField, JobApplicationDto::getNextActionDate, JobApplicationDto::setNextActionDate);
        binder.bind(memoField, JobApplicationDto::getMemo, JobApplicationDto::setMemo);

        binder.readBean(job);

        layout.add(companyNameField, priorityField, actionLayout);
        add(layout);

        // ボタンエリア
        Button saveButton = new Button("保存", e -> {
            try {
                binder.writeBean(job);
                onSave.accept(job); // ★ここで親(MainView)に渡すのも DTO になる
                close();
            } catch (Exception validationEx) {
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button deleteButton = new Button("削除", e -> {
            onDelete.accept(job); // ★ここも DTO
            close();
        });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteButton.setVisible(job.getId() != null);

        getFooter().add(deleteButton);
        getFooter().add(new Button("キャンセル", e -> close()));
        getFooter().add(saveButton);
    }
}