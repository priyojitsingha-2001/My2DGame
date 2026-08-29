package main;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("2d Adventure");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack(); // Causes this window to be sized to fit the preffered size and layouts of its subcomponents (=GamePanel)

        window.setLocationRelativeTo(null); //Not specify the loactio of the window = the window will be displayed at the center of the screen
        window.setVisible(true);
    }
}
