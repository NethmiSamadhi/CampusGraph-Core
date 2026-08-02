import ui.gui.MainWindow;

import javax.swing.*;

/**
 * Entry point for the desktop GUI version of the app.
 * Run: java -cp out MainGui   (after compiling with javac)
 *
 * The original console version is still available via Main.java.
 */
public class MainGui {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}
