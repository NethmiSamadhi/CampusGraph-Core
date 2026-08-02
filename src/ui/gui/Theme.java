package ui.gui;

import java.awt.Color;
import java.awt.Font;

/**
 * Design tokens for the desktop UI.
 *
 * Design direction: the app IS a campus map, so the visual language borrows
 * from architectural blueprints and physical wayfinding signage rather than
 * a generic "dashboard" look. Deep blueprint-navy canvas with cyan linework
 * for the map itself; a warm amber "trail marker" accent (the colour used on
 * real trailhead signs) for the computed route; and a cream "ticket/receipt"
 * panel for the route printout, evoking a printed wayfinding slip you'd get
 * from an information desk.
 *
 * Two type roles: Segoe UI for interface chrome (labels, buttons), and
 * Consolas (monospace) for anything that reads as technical/measured data —
 * blueprint annotations and the route ticket — echoing hand-labelled plans.
 */
public final class Theme {
    private Theme() {}

    // Blueprint canvas
    public static final Color NAVY_BG = new Color(0x0F, 0x2A, 0x43);
    public static final Color NAVY_SIDEBAR = new Color(0x0A, 0x1F, 0x33);
    public static final Color GRID_LINE = new Color(0x1B, 0x3A, 0x54);
    public static final Color CYAN_LINE = new Color(0x7F, 0xDB, 0xFF);
    public static final Color CYAN_DIM = new Color(0x4E, 0x8B, 0xAD);

    // Accents
    public static final Color AMBER = new Color(0xF2, 0xA9, 0x3B);
    public static final Color AMBER_DIM = new Color(0x8A, 0x63, 0x28);
    public static final Color GREEN_START = new Color(0x4C, 0xD9, 0x7B);
    public static final Color RED_END = new Color(0xFF, 0x6B, 0x57);

    // Ticket / output panel
    public static final Color PAPER = new Color(0xFB, 0xF7, 0xEE);
    public static final Color PAPER_INK = new Color(0x1F, 0x2A, 0x33);

    // Text on navy
    public static final Color TEXT_LIGHT = new Color(0xE8, 0xF1, 0xF8);
    public static final Color TEXT_DIM = new Color(0x9C, 0xB7, 0xC9);

    public static final Font FONT_CHROME_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_CHROME = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_MONO = new Font("Consolas", Font.PLAIN, 12);
    public static final Font FONT_MONO_BOLD = new Font("Consolas", Font.BOLD, 12);
    public static final Font FONT_MONO_LABEL = new Font("Consolas", Font.PLAIN, 11);
}
