package ru.nsu.belousova.client.gui;

import javax.swing.*;

public class GameFrame extends JFrame {
    public GameFrame() {
        super("Worms");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    public void setPanel(JPanel panel) {
        getContentPane().removeAll();
        getContentPane().add(panel);
        pack();
    }
}
