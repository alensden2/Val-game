package org.example;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Random;
import javax.swing.*;

public class Valgame extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 1500;
    int boardHeight = 700;

    //image
    Image backgroundImage;
    Image alenImg;
    Image gems;
    Image rose;
    Image buildingImg;

    //Alen
    int alenX = boardWidth/8;
    int alenY = boardHeight/2;
    int alenWidth = 70;
    int alenHeight = 70;

    // game logic
    Alen alen;
    int velocityY = -0;
    int velocityX = -4;
    int gravity = 1;
    Timer gameLoop;
    Timer placeBuildingsTimer;

    List<Building> buildings;

    //building
    int buildingX = boardWidth;
    int buildingY = boardHeight;
    int buildingWidth = 64;
    int buildingHeight = 512;

    Valgame() {
        setFocusable(true);
        addKeyListener(this);
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        backgroundImage = new ImageIcon(getClass().getResource("./background.png")).getImage();
        alenImg = new ImageIcon(getClass().getResource("./Alen.png")).getImage();
        gems = new ImageIcon(getClass().getResource("./crystal.jpeg")).getImage();
        rose = new ImageIcon(getClass().getResource("./rose.png")).getImage();
        buildingImg = new ImageIcon(getClass().getResource("./Building.png")).getImage();
        alen = new Alen(alenX,alenY,alenWidth,alenHeight,alenImg);
        gameLoop = new Timer(1000/60, this);
        placeBuildingsTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeBuildings();
            }
        });
        gameLoop.start();
    }

    public void placeBuildings() {
        Building building = new Building(buildingX,buildingY,buildingWidth,buildingHeight,buildingImg);
        buildings.add(building);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void move() {
        // move bird
        velocityY = velocityY+gravity;
        alen.y += velocityY;
        alen.y = Math.max(alen.y, 0);
    }

    public void draw(Graphics g){
        g.drawImage(backgroundImage, 0, 0, boardWidth,boardHeight, null);

        //alen
        g.drawImage(alenImg, alen.x, alen.y, alen.width, alen.height, null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            velocityY = -9;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
