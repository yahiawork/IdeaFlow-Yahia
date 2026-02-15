package com.yahia.ideaflow.theme;

import java.awt.Color;

/**
 * Central palette (light/dark).
 * No external theming libraries, just our own palette.
 */
public final class Colors {

    public static final boolean DARK = false;

    public static Color BG;
    public static Color SURFACE;
    public static Color SURFACE_2;
    public static Color TEXT;
    public static Color MUTED_TEXT;
    public static Color OUTLINE;
    public static Color ACCENT;
    public static Color ACCENT_2;
    public static Color DANGER;

    static {
        setLight();
    }

    private Colors() {}

    public static void toggle() { /* dark mode disabled */ setLight(); }

    private static void setLight() {
        BG = new Color(245, 246, 248);
        SURFACE = new Color(255, 255, 255);
        SURFACE_2 = new Color(250, 251, 252);
        TEXT = new Color(22, 24, 28);
        MUTED_TEXT = new Color(104, 112, 124);
        OUTLINE = new Color(225, 229, 235);
        ACCENT = new Color(70, 97, 255);
        ACCENT_2 = new Color(128, 146, 255);
        DANGER = new Color(232, 72, 85);
    }

    private static void setDark() {
        BG = new Color(14, 16, 20);
        SURFACE = new Color(20, 22, 28);
        SURFACE_2 = new Color(26, 29, 36);
        TEXT = new Color(235, 238, 245);
        MUTED_TEXT = new Color(155, 163, 176);
        OUTLINE = new Color(44, 48, 58);
        ACCENT = new Color(120, 140, 255);
        ACCENT_2 = new Color(90, 112, 255);
        DANGER = new Color(255, 90, 103);
    }
}
