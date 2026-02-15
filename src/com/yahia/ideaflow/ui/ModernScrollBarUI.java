package com.yahia.ideaflow.ui;

import com.yahia.ideaflow.theme.Colors;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public final class ModernScrollBarUI extends BasicScrollBarUI {

    private final int thickness;

    public ModernScrollBarUI(int thickness) {
        this.thickness = Math.max(10, thickness);
    }

    @Override
    protected void configureScrollBarColors() {
        thumbColor = Colors.OUTLINE;
        trackColor = Colors.BG;
    }

    @Override
    protected JButton createDecreaseButton(int orientation) { return zeroButton(); }

    @Override
    protected JButton createIncreaseButton(int orientation) { return zeroButton(); }

    private JButton zeroButton() {
        JButton b = new JButton();
        b.setPreferredSize(new Dimension(0,0));
        b.setMinimumSize(new Dimension(0,0));
        b.setMaximumSize(new Dimension(0,0));
        b.setOpaque(false);
        b.setBorder(BorderFactory.createEmptyBorder());
        b.setContentAreaFilled(false);
        return b;
    }

    @Override
    protected Dimension getMinimumThumbSize() {
        return new Dimension(thickness, 36);
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        // invisible track
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
        if (r.isEmpty() || !scrollbar.isEnabled()) return;

        Graphics2D g2 = Gfx.setup(g);
        g2.setColor(new Color(Colors.OUTLINE.getRed(), Colors.OUTLINE.getGreen(), Colors.OUTLINE.getBlue(), 170));

        int arc = 999;
        int pad = 3;
        int x = r.x + pad;
        int y = r.y + pad;
        int w = r.width - pad * 2;
        int h = r.height - pad * 2;

        g2.fillRoundRect(x, y, w, h, arc, arc);
        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        if (scrollbar.getOrientation() == Adjustable.VERTICAL) {
            return new Dimension(thickness, super.getPreferredSize(c).height);
        }
        return new Dimension(super.getPreferredSize(c).width, thickness);
    }
}
