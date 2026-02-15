package com.yahia.ideaflow.ui;

import com.yahia.ideaflow.theme.Colors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Soft modern button (rounded + hover animation).
 */
public class RoundedButton extends JButton {

    public enum Kind { PRIMARY, GHOST, DANGER }

    private final int radius;
    private final Kind kind;

    private float hover = 0f; // 0..1
    private final Timer anim;

    public RoundedButton(String text, Kind kind, int radius) {
        super(text);
        this.kind = kind;
        this.radius = radius;

        setOpaque(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));

        anim = new Timer(16, e -> {
            float target = isRollover() ? 1f : 0f;
            hover += (target - hover) * 0.10f;
            if (Math.abs(target - hover) < 0.02f) hover = target;
            repaint();
        });
        anim.start();

        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { repaint(); }
            @Override public void mouseExited(MouseEvent e) { repaint(); }
        });
    }

    private boolean isRollover() {
        return getModel().isRollover() && isEnabled();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Gfx.setup(g);

        int w = getWidth();
        int h = getHeight();

        Color bg;
        Color fg;
        Color outline = null;

        if (kind == Kind.PRIMARY) {
            bg = blend(Colors.ACCENT, Colors.ACCENT_2, hover * 0.5f);
            fg = Colors.SURFACE;
        } else if (kind == Kind.DANGER) {
            bg = blend(Colors.DANGER, new Color(255, 120, 130), hover * 0.5f);
            fg = Colors.SURFACE;
        } else { // GHOST
            bg = blend(Colors.SURFACE, Colors.SURFACE_2, hover);
            fg = Colors.TEXT;
            outline = Colors.OUTLINE;
        }

        // Press effect
        if (getModel().isPressed()) {
            bg = blend(bg, Colors.OUTLINE, 0.18f);
        }

        Gfx.fillRoundRect(g2, 0, 0, w, h, radius, bg);
        if (outline != null) {
            g2.setStroke(new BasicStroke(1f));
            Gfx.drawRoundRect(g2, 0, 0, w - 1, h - 1, radius, outline);
        }

        // text
        g2.setColor(fg);
        FontMetrics fm = g2.getFontMetrics();
        int tx = (w - fm.stringWidth(getText())) / 2;
        int ty = (h + fm.getAscent() - fm.getDescent()) / 2;
        g2.drawString(getText(), tx, ty);

        g2.dispose();
    }

    private static Color blend(Color a, Color b, float t) {
        t = Math.max(0f, Math.min(1f, t));
        int r = (int) (a.getRed() + (b.getRed() - a.getRed()) * t);
        int g = (int) (a.getGreen() + (b.getGreen() - a.getGreen()) * t);
        int bl = (int) (a.getBlue() + (b.getBlue() - a.getBlue()) * t);
        return new Color(r, g, bl);
    }
}
