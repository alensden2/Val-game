package org.example;

import javax.swing.*;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        int boardWidth = 1500;
        int boardHeight = 700;

        JFrame frame = new JFrame("Val Game");
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Valgame valgame= new Valgame();
        frame.add(valgame);
        frame.pack();
        valgame.requestFocus();
        frame.setVisible(true);
    }
}