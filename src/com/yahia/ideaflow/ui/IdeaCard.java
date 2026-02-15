package com.yahia.ideaflow.ui;

import com.yahia.ideaflow.model.Idea;
import com.yahia.ideaflow.theme.Colors;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

/**
 * A clickable modern card representing an Idea.
 */
public final class IdeaCard extends RoundedPanel {

    private final Idea idea;

    public IdeaCard(Idea idea, Consumer<Idea> onOpen) {
        super(18, Colors.SURFACE);
        this.idea = idea;

        setShadow(true);
        setOutline(true);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel title = new JLabel(idea.title);
        title.setForeground(Colors.TEXT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 14f));

        JLabel meta = new JLabel(metaText());
        meta.setForeground(Colors.MUTED_TEXT);

        JTextArea body = new JTextArea(idea.body == null ? "" : idea.body);
        body.setOpaque(false);
        body.setEditable(false);
        body.setLineWrap(true);
        body.setWrapStyleWord(true);
        body.setForeground(Colors.MUTED_TEXT);
        body.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(title, BorderLayout.NORTH);
        top.add(meta, BorderLayout.SOUTH);

        add(top, BorderLayout.NORTH);
        add(body, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        bottom.setOpaque(false);
        bottom.add(new Chip("Score: " + idea.score()));
        bottom.add(new Chip("Energy: " + idea.energy));
        bottom.add(new Chip("Status: " + idea.status));
        add(bottom, BorderLayout.SOUTH);

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) { onOpen.accept(idea); }
            @Override public void mouseEntered(java.awt.event.MouseEvent e) { setFill(Colors.SURFACE_2); }
            @Override public void mouseExited(java.awt.event.MouseEvent e) { setFill(Colors.SURFACE); }
        });
    }

    private String metaText() {
        return "Urg " + idea.urgency + "  •  Impact " + idea.impact + "  •  Effort " + idea.effort;
    }

    private static final class Chip extends JComponent {
        private final String text;

        Chip(String text) {
            this.text = text;
            setPreferredSize(new Dimension(10, 26));
        }

@Override
        public Dimension getPreferredSize() {
            FontMetrics fm = getFontMetrics(getFont());
            int w = 20 + fm.stringWidth(text);
            return new Dimension(w, 26);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = Gfx.setup(g);
            int w = getWidth(), h = getHeight();
            Color bg = Colors.DARK ? Colors.SURFACE_2 : Colors.BG;
            Gfx.fillRoundRect(g2, 0, 0, w, h, 999, bg);
            g2.setColor(new Color(Colors.OUTLINE.getRed(), Colors.OUTLINE.getGreen(), Colors.OUTLINE.getBlue(), 160));
            g2.drawRoundRect(0, 0, w - 1, h - 1, 999, 999);

            g2.setColor(Colors.MUTED_TEXT);
            FontMetrics fm = g2.getFontMetrics();
            int tx = 10;
            int ty = (h + fm.getAscent() - fm.getDescent()) / 2;
            g2.drawString(text, tx, ty);
            g2.dispose();
        }
    }
}
