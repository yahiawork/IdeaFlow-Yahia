package com.yahia.ideaflow.ui;

import com.yahia.ideaflow.theme.Colors;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.function.Consumer;

/**
 * Rounded search field with placeholder and change callback.
 */
public class SearchField extends JTextField {

    private String placeholder = "Search ideas...";
    private Consumer<String> onChange;

    public SearchField() {
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        setForeground(Colors.TEXT);
        setCaretColor(Colors.ACCENT);

        getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { fire(); }
            @Override public void removeUpdate(DocumentEvent e) { fire(); }
            @Override public void changedUpdate(DocumentEvent e) { fire(); }
            private void fire() { if (onChange != null) onChange.accept(getText()); }
        });
    }

    public void setOnChange(Consumer<String> onChange) {
        this.onChange = onChange;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Gfx.setup(g);
        int w = getWidth();
        int h = getHeight();

        // background + outline
        Gfx.fillRoundRect(g2, 0, 0, w, h, 16, Colors.SURFACE);
        g2.setStroke(new BasicStroke(1f));
        Gfx.drawRoundRect(g2, 0, 0, w - 1, h - 1, 16, Colors.OUTLINE);

        super.paintComponent(g);

        // placeholder
        if (getText().isEmpty() && !isFocusOwner()) {
            g2.setColor(Colors.MUTED_TEXT);
            g2.setFont(getFont());
            Insets in = getInsets();
            FontMetrics fm = g2.getFontMetrics();
            int y = (h + fm.getAscent() - fm.getDescent()) / 2;
            g2.drawString(placeholder, in.left, y);
        }

        g2.dispose();
    }
}
