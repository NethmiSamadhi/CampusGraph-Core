package ui.gui;

import javax.swing.*;
import java.awt.*;

/**
 * A flat, rounded-rectangle button used instead of Swing's default beveled
 * button, so the control panel reads as an intentional interface rather than
 * an unstyled Java desktop app.
 */
public class RoundedButton extends JButton {

    private final Color baseColor;
    private final Color hoverColor;
    private final Color textColor;

    public RoundedButton(String text, Color baseColor, Color hoverColor, Color textColor) {
        super(text);
        this.baseColor = baseColor;
        this.hoverColor = hoverColor;
        this.textColor = textColor;
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setForeground(textColor);
        setFont(Theme.FONT_CHROME_BOLD);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));

        getModel().addChangeListener(e -> repaint());
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getModel().isRollover() || getModel().isPressed() ? hoverColor : baseColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
        g2.dispose();
        super.paintComponent(g);
    }
}
