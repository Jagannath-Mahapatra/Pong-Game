package PongGame;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    GamePanel panel;
    Container container;
    GameFrame() throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        container = getContentPane();
        setSize(GamePanel.PANEL_WIDTH, GamePanel.PANEL_HEIGHT);
        setTitle("Pong Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        this.setBackground(Color.black);
        panel = new GamePanel();
        container.add(panel);
        setVisible(true);
    }
}
