package com.yahia.ideaflow.theme;

import javax.swing.*;
import java.awt.*;

/**
 * Applies Nimbus (built-in) with UI defaults tuned to match our palette.
 * Still pure Swing: no downloads.
 */
public final class Theme {

    private Theme() {}

    public static void applyNimbusModern() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        Font base = new Font("SansSerif", Font.PLAIN, 13);
        setFontForAll(base);

        // Basic background and text
        UIManager.put("control", Colors.BG);
        UIManager.put("info", Colors.BG);
        UIManager.put("text", Colors.TEXT);
        UIManager.put("nimbusBase", Colors.ACCENT);
        UIManager.put("nimbusBlueGrey", Colors.OUTLINE);
        UIManager.put("nimbusLightBackground", Colors.SURFACE);

        // Focus ring
        UIManager.put("nimbusFocus", Colors.ACCENT);

        // Common component tweaks
        UIManager.put("ScrollBar.thumb", Colors.OUTLINE);
        UIManager.put("ScrollBar.thumbDarkShadow", Colors.OUTLINE);
        UIManager.put("ScrollBar.thumbHighlight", Colors.OUTLINE);
        UIManager.put("ScrollBar.track", Colors.BG);

        // Less harsh selections
        UIManager.put("List.selectionBackground", Colors.ACCENT_2);
        UIManager.put("List.selectionForeground", Colors.SURFACE);

        UIManager.put("Table.selectionBackground", Colors.ACCENT_2);
        UIManager.put("Table.selectionForeground", Colors.SURFACE);

        // Remove noisy borders in many Nimbus components
        UIManager.put("nimbusBorder", Colors.OUTLINE);

        // Option pane (dialogs)
        UIManager.put("OptionPane.background", Colors.SURFACE);
        UIManager.put("Panel.background", Colors.BG);
    }

    private static void setFontForAll(Font f) {
        var keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof Font) {
                UIManager.put(key, f);
            }
        }
    }
}
