package com.yahia.ideaflow.ui;

import com.yahia.ideaflow.theme.Colors;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

/**
 * Left navigation: modern soft buttons with selection indicator.
 */
public final class Sidebar extends RoundedPanel {

    private String selected = "inbox";
    private final Consumer<String> onNavigate;

    private final SideItem inbox;
    private final SideItem board;
    private final SideItem plans;
    private final SideItem tags;
    private final SideItem stats;
    private final SideItem settings;

    public Sidebar(Consumer<String> onNavigate) {
        super(18, Colors.SURFACE);
        this.onNavigate = onNavigate;

        setShadow(true);
        setOutline(true);
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(220, 0));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(6, 0, 6, 0);

        JLabel title = new JLabel("IdeaFlow");
        JLabel sub = new JLabel("Workspace");
        sub.setForeground(Colors.MUTED_TEXT);
        sub.setFont(sub.getFont().deriveFont(Font.PLAIN, 12f));
        title.setForeground(Colors.TEXT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        add(title, gc);

        gc.gridy++;
        add(sub, gc);

        gc.gridy++;
        add(space(6), gc);

        inbox = new SideItem("Inbox");
        board = new SideItem("Board");
        plans = new SideItem("Plans");
        tags = new SideItem("Tags");
        stats = new SideItem("Stats");
        settings = new SideItem("Settings");

        addItem(gc, inbox, "inbox");
        addItem(gc, board, "board");

        // placeholders (not implemented screens, but kept for project structure)
        addItem(gc, plans, "inbox");
        addItem(gc, tags, "inbox");
        addItem(gc, stats, "inbox");
        addItem(gc, settings, "inbox");

        gc.gridy++;
        gc.weighty = 1;
        add(Box.createVerticalGlue(), gc);

        setSelected("inbox");
    }

    private void addItem(GridBagConstraints gc, SideItem item, String route) {
        gc.gridy++;
        add(item, gc);
        item.setOnClick(() -> {
            setSelected(route.equals("board") ? "board" : "inbox");
            onNavigate.accept(route);
        });
    }

    private JComponent space(int h) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(1, h));
        return p;
    }

    private void setSelected(String key) {
        selected = key;
        inbox.setSelected("inbox".equals(key));
        board.setSelected("board".equals(key));
        plans.setSelected(false);
        tags.setSelected(false);
        stats.setSelected(false);
        settings.setSelected(false);
    }

    private static final class SideItem extends JComponent {
        private boolean selected = false;
        private Runnable onClick;

        SideItem(String label) {
            setPreferredSize(new Dimension(180, 42));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setToolTipText(label);

            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (onClick != null) onClick.run();
                }
                @Override public void mouseEntered(java.awt.event.MouseEvent e) { repaint(); }
                @Override public void mouseExited(java.awt.event.MouseEvent e) { repaint(); }
            });

            putClientProperty("label", label);
        }

        void setOnClick(Runnable r) { this.onClick = r; }
        void setSelected(boolean s) { this.selected = s; repaint(); }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = Gfx.setup(g);
            int w = getWidth();
            int h = getHeight();

            boolean hover = getMousePosition() != null;

            Color bg = Colors.SURFACE;
            if (selected) bg = Colors.SURFACE_2;
            else if (hover) bg = Colors.SURFACE_2;

            // background
            Gfx.fillRoundRect(g2, 0, 0, w, h, 14, bg);

            // left indicator
            if (selected) {
                g2.setColor(Colors.ACCENT);
                g2.fillRoundRect(0, 10, 5, h - 20, 8, 8);
            }

            // text
            String label = (String) getClientProperty("label");
            g2.setFont(getFont().deriveFont(selected ? Font.BOLD : Font.PLAIN));
            g2.setColor(selected ? Colors.TEXT : Colors.MUTED_TEXT);
            FontMetrics fm = g2.getFontMetrics();
            int tx = 14;
            int ty = (h + fm.getAscent() - fm.getDescent()) / 2;
            g2.drawString(label, tx, ty);

            g2.dispose();
        }
    }
}
