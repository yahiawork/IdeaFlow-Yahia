package com.yahia.ideaflow.ui;

import com.yahia.ideaflow.theme.Colors;

import javax.swing.*;
import java.awt.*;

public final class TopBar extends JPanel {

    public TopBar(Runnable onQuickAdd, java.util.function.Consumer<String> onSearch) {
        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(12, 12, 0, 12));

        RoundedPanel card = new RoundedPanel(18, Colors.SURFACE);
        card.setShadow(true);
        card.setOutline(true);
        card.setLayout(new BorderLayout(12, 12));
        card.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setOpaque(false);

        JLabel badge = new JLabel("  IF  ");
        badge.setOpaque(true);
        badge.setForeground(Colors.SURFACE);
        badge.setBackground(Colors.ACCENT);
        badge.setFont(badge.getFont().deriveFont(Font.BOLD, 12f));
        badge.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

        JLabel title = new JLabel("IdeaFlow");
        title.setForeground(Colors.TEXT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));

        left.add(badge);
        left.add(title);
        card.add(left, BorderLayout.WEST);

        SearchField search = new SearchField();
        search.setPlaceholder("Search ideas, plans, tags...");
        search.setOnChange(onSearch);
        card.add(search, BorderLayout.CENTER);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        RoundedButton addBtn = new RoundedButton("+ Add", RoundedButton.Kind.PRIMARY, 14);
        addBtn.addActionListener(e -> onQuickAdd.run());
card.add(right, BorderLayout.EAST);

        add(card, BorderLayout.CENTER);
    }
}
