package com.yahia.ideaflow.ui;

import com.yahia.ideaflow.theme.Colors;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Animated screen navigator:
 * - Register screens by key
 * - Switch with fade + slide animation (pure Swing, no libs)
 *
 * How it works:
 * - Keeps a current component and a target component (Card-style)
 * - On navigation: takes snapshots (BufferedImage) of both
 * - Paints an animated transition between the images using a Timer
 */
public final class AnimatedNavigator extends JComponent {

    public enum Direction { LEFT, RIGHT }

    private final Map<String, JComponent> screens = new HashMap<>();

    private String currentKey;

    // Animation state
    private BufferedImage fromImg;
    private BufferedImage toImg;
    private float t = 1f; // 0..1
    private boolean animating = false;
    private Direction direction = Direction.LEFT;

    private final Timer timer;

    public AnimatedNavigator() {
        setOpaque(false);
        setLayout(null); // we manually size the active screen
        timer = new Timer(15, e -> tick());
    }

    public void addScreen(String key, JComponent screen) {
        screens.put(key, screen);
        screen.setOpaque(false);
        if (currentKey == null) {
            currentKey = key;
            add(screen);
            screen.setBounds(0, 0, getWidth(), getHeight());
        }
    }

    public JComponent getScreen(String key) {
        return screens.get(key);
    }

    public String getCurrentKey() {
        return currentKey;
    }

    public void show(String key) {
        show(key, Direction.LEFT);
    }

    public void show(String key, Direction dir) {
        if (key == null || key.equals(currentKey)) return;
        if (!screens.containsKey(key)) return;

        if (getWidth() <= 0 || getHeight() <= 0) {
            // if not laid out yet, just swap without animation
            swapTo(key);
            return;
        }

        this.direction = dir;

        // Snapshot current and target
        fromImg = snapshotOf(currentComponent());
        JComponent target = screens.get(key);
        prepareTarget(target);
        toImg = snapshotOf(target);

        // Start anim
        animating = true;
        t = 0f;
        timer.start();

        // After snapshot, swap real component immediately (for events), but keep animation drawn by images.
        swapTo(key);
    }

    private void swapTo(String key) {
        removeAll();
        JComponent next = screens.get(key);
        add(next);
        currentKey = key;
        next.setBounds(0, 0, getWidth(), getHeight());
        revalidate();
        repaint();
    }

    private JComponent currentComponent() {
        if (currentKey == null) return null;
        return screens.get(currentKey);
    }

    private void prepareTarget(JComponent comp) {
        // Make sure it has correct size for snapshot
        comp.setBounds(0, 0, getWidth(), getHeight());
        comp.doLayout();
        comp.validate();
    }

    private BufferedImage snapshotOf(JComponent comp) {
        if (comp == null) return null;
        BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Fill background (so snapshots look consistent)
        g2.setColor(Colors.BG);
        g2.fillRect(0, 0, getWidth(), getHeight());

        comp.printAll(g2);
        g2.dispose();
        return img;
    }

    private void tick() {
        if (!animating) {
            timer.stop();
            return;
        }
        t += 0.028f; // speed (slow & smooth)
        if (t >= 1f) {
            t = 1f;
            animating = false;
            timer.stop();
            fromImg = null;
            toImg = null;
        }
        repaint();
    }

    @Override
    public void doLayout() {
        super.doLayout();
        Component[] comps = getComponents();
        for (Component c : comps) {
            c.setBounds(0, 0, getWidth(), getHeight());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        // When animating, paint animated images on top of normal component painting
        super.paintComponent(g);

        if (!animating || fromImg == null || toImg == null) return;

        Graphics2D g2 = Gfx.setup(g);

        int w = getWidth();
        int h = getHeight();

        // easing (smooth)
        float eased = easeOutCubic(t);

        // slide distance (small, modern)
        int dx = (int) (w * 0.18f);

        int sign = (direction == Direction.LEFT) ? -1 : 1;

        int fromX = (int) (eased * dx * sign);
        int toX = fromX - (sign * dx);

        // Fade + slide + subtle scale (stronger, modern transition)
float scaleFrom = 1.00f + (0.02f * eased);   // fromImg slightly grows out
float scaleTo   = 0.98f + (0.02f * eased);   // toImg scales in

// Draw from
g2.setComposite(AlphaComposite.SrcOver.derive(1f - eased));
drawScaled(g2, fromImg, fromX, 0, scaleFrom);

// Draw to
g2.setComposite(AlphaComposite.SrcOver.derive(eased));
drawScaled(g2, toImg, toX, 0, scaleTo);
g2.dispose();
    }

private static void drawScaled(Graphics2D g2, BufferedImage img, int x, int y, float scale) {
    if (img == null) return;
    int w = img.getWidth();
    int h = img.getHeight();
    int cx = x + w / 2;
    int cy = y + h / 2;

    java.awt.geom.AffineTransform old = g2.getTransform();
    java.awt.geom.AffineTransform at = new java.awt.geom.AffineTransform();
    at.translate(cx, cy);
    at.scale(scale, scale);
    at.translate(-w / 2.0, -h / 2.0);
    g2.setTransform(at);
    g2.drawImage(img, 0, 0, null);
    g2.setTransform(old);
}

private static float easeOutCubic(float x) {

        float p = 1f - x;
        return 1f - p * p * p;
    }
}
