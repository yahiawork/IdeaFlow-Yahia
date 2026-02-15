package com.yahia.ideaflow.ui;

import javax.swing.*;
import java.awt.*;
import com.yahia.ideaflow.theme.Colors;

/**
 * Rounded container with optional shadow and outline.
 */
public class RoundedPanel extends JPanel {
    private final int radius;
    private Color fill;
    private boolean shadow = false;
    private boolean outline = false;

    public RoundedPanel(int radius, Color fill) {
        this.radius = radius;
        this.fill = fill;
        setOpaque(false);
    }

    public RoundedPanel setFill(Color c) { this.fill = c; repaint(); return this; }
    public RoundedPanel setShadow(boolean v) { this.shadow = v; repaint(); return this; }
    public RoundedPanel setOutline(boolean v) { this.outline = v; repaint(); return this; }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Gfx.setup(g);
        int w = getWidth();
        int h = getHeight();

        if (shadow) {
            // Keep shadow inside bounds by leaving padding in layout/border.
            Gfx.paintSoftShadow(g2, 8, 8, w - 16, h - 16, radius);
        }

        int pad = shadow ? 8 : 0;
        Gfx.fillRoundRect(g2, pad, pad, w - pad * 2, h - pad * 2, radius, fill);

        if (outline) {
            g2.setStroke(new BasicStroke(1f));
            Gfx.drawRoundRect(g2, pad, pad, w - pad * 2 - 1, h - pad * 2 - 1, radius, Colors.OUTLINE);
        }

        g2.dispose();
        super.paintComponent(g);
    }
}
