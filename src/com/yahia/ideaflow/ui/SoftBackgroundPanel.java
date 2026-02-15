package com.yahia.ideaflow.ui;

import com.yahia.ideaflow.theme.Colors;

import javax.swing.*;
import java.awt.*;

public final class SoftBackgroundPanel extends JPanel {
    public SoftBackgroundPanel() {
        setOpaque(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = Gfx.setup(g);

        int w = getWidth(), h = getHeight();
        Color a = Colors.BG;
        Color b = new Color(Colors.ACCENT.getRed(), Colors.ACCENT.getGreen(), Colors.ACCENT.getBlue(), Colors.DARK ? 18 : 12);

        g2.setPaint(new GradientPaint(0, 0, a, w, h, b));
        g2.fillRect(0, 0, w, h);
        g2.dispose();
    }
}
