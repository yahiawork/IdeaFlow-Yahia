package com.yahia.ideaflow.app;

import javax.swing.SwingUtilities;
import com.yahia.ideaflow.theme.Theme;

/**
 * Entry point.
 * Pure Swing, no external dependencies.
 */
public final class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Theme.applyNimbusModern();
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
