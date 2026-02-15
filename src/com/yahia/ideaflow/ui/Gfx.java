package com.yahia.ideaflow.ui;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Small drawing helpers.
 */
public final class Gfx {
    private Gfx(){}

    public static Graphics2D setup(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        return g2;
    }

    public static void fillRoundRect(Graphics2D g2, int x, int y, int w, int h, int r, Color c) {
        g2.setColor(c);
        g2.fill(new RoundRectangle2D.Float(x, y, w, h, r, r));
    }

    public static void drawRoundRect(Graphics2D g2, int x, int y, int w, int h, int r, Color c) {
        g2.setColor(c);
        g2.draw(new RoundRectangle2D.Float(x, y, w, h, r, r));
    }

    /**
     * Very lightweight "shadow": draws several expanded rounded rectangles with low alpha.
     * Looks soft and modern without heavy blur.
     */
    public static void paintSoftShadow(Graphics2D g2, int x, int y, int w, int h, int r) {
        for (int i = 8; i >= 1; i--) {
            int alpha = (int) (10 + (8 - i) * 3); // small alpha range
            g2.setColor(new Color(0, 0, 0, alpha));
            g2.fill(new RoundRectangle2D.Float(
                    x - i, y - i, w + i * 2, h + i * 2,
                    r + i, r + i
            ));
        }
    }
}
