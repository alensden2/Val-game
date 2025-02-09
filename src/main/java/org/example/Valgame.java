package org.example;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Random;
import javax.swing.*;

public class Valgame extends JPanel {
    int boardWidth = 1500;
    int boardHeight = 700;

    //image
    Image backgroundImage;
    Image alenImg;
    Image gems;
    Image rose;
    Image building;

    //Alen
    int alenX = boardWidth/8;
    int alenY = boardHeight/2;
    int alenWidth = 70;
    int alenHeight = 70;

    // game logic
    Alen alen;

    Timer gameLoop

    Valgame() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        backgroundImage = new ImageIcon(getClass().getResource("./background.png")).getImage();
        alenImg = new ImageIcon(getClass().getResource("./Alen.png")).getImage();
        gems = new ImageIcon(getClass().getResource("./crystal.jpeg")).getImage();
        rose = new ImageIcon(getClass().getResource("./rose.png")).getImage();
        building = new ImageIcon(getClass().getResource("./Building.avif")).getImage();
        alen = new Alen(alenX,alenY,alenWidth,alenHeight,alenImg);


    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        g.drawImage(backgroundImage, 0, 0, boardWidth,boardHeight, null);

        //alen
        g.drawImage(alenImg, alen.x, alen.y, alen.width, alen.height, null);
    }
}
