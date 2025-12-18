package com.example.portfolio.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.theme.lumo.Lumo; // ★テーマ用

@Layout
public class MainLayout extends AppLayout {

    public MainLayout() {
        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1("就活ポートフォリオ");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");

        SideNav nav = new SideNav();
        nav.addItem(new SideNavItem("進捗ボード", MainView.class, VaadinIcon.DASHBOARD.create()));
        nav.addItem(new SideNavItem("カレンダー", CalendarView.class, VaadinIcon.CALENDAR.create()));

        Button themeToggle = new Button(VaadinIcon.MOON_O.create(), click -> {
            var themeList = getElement().getThemeList(); // アプリ全体のテーマリスト

            if (themeList.contains(Lumo.DARK)) {
                // ダークモードなら解除（ライトモードへ）
                themeList.remove(Lumo.DARK);
                click.getSource().setIcon(VaadinIcon.MOON_O.create()); // 月アイコンに戻す
            } else {
                // ライトモードならダークモードへ
                themeList.add(Lumo.DARK);
                click.getSource().setIcon(VaadinIcon.SUN_O.create());  // 太陽アイコンにする
            }
        });
        // ボタンの見た目をシンプルに
        themeToggle.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        // ナビバーに要素を追加（タイトルとボタンを離して配置したいので工夫はいりますが、一旦シンプルに並べます）
        addToNavbar(toggle, title, themeToggle);

        // もし「右端にボタンを寄せたい」なら、以下のスタイル設定が有効です
        title.getStyle().set("margin-right", "auto"); // タイトルの右側を余白で埋める

        addToDrawer(nav);
    }
}