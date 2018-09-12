package edu.cmu.cs.cs214.hw4.gui;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Carcassone game application.
 */
public class CarcassonneMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Start New Carcassonne Game");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.add(new SimpleGameStart(frame));

            frame.pack();
            frame.setResizable(false);
            frame.setVisible(true);
        });
    }
}
