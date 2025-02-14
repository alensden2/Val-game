package org.example;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
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

    boolean gameOver = false;
    // game logic
    Alen alen;
    int velocityY = -0;
    int velocityX = -4;
    int gravity = 1;
    Timer gameLoop;
    Timer placeBuildingsTimer;
    double score = 0;

    List<Building> buildings;
    Random random = new Random();

    //building
    int buildingX = boardWidth;
    int buildingWidth = 120;
    int buildingHeight = 306;
    int buildingY = boardHeight - buildingHeight;

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
        buildings = new ArrayList<Building>();
        placeBuildingsTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeBuildings();
            }
        });
        placeBuildingsTimer.start();
        gameLoop.start();
    }

    public void placeBuildings() {
        int minY = boardHeight - buildingHeight; // Bottom of the screen
        int maxY = boardHeight - (buildingHeight / 2);
        int randomBuildingY = minY + (int) (Math.random() * (maxY - minY));
        Building building = new Building(buildingX,buildingY,buildingWidth,buildingHeight,buildingImg);
        building.y = randomBuildingY;
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

        // move building
        for(int i = 0; i <buildings.size(); i++){
            Building building = buildings.get(i);
            building.x += velocityX;

            if(!building.passed && alen.x > building.x + building.width){
                score += 1.0;
                building.passed = true;
            }
        }

        // bird collision
        if ( alen.y > boardHeight) {
           gameOver = true;
        }
    }

    public void draw(Graphics g){
        g.drawImage(backgroundImage, 0, 0, boardWidth,boardHeight, null);

        //alen
        g.drawImage(alenImg, alen.x, alen.y, alen.width, alen.height, null);

        // buildings
        for(int i = 0; i <buildings.size(); i++){
            Building building = buildings.get(i);
            g.drawImage(building.img, building.x, building.y, building.width, building.height, null);

            if (collision(alen, building)){
                gameOver = true;
            }
        }

        //Score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (gameOver) {
            g.drawString("Game Over : " + String.valueOf((int) score), 10,35);
        }
        else {
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    public boolean collision(Alen a, Building b){
        return a.x < b.x + b.width &&   //a's top left corner doesn't reach b's top right corner
                a.x + a.width > b.x &&   //a's top right corner passes b's top left corner
                a.y < b.y + b.height &&  //a's top left corner doesn't reach b's bottom left corner
                a.y + a.height > b.y;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            placeBuildingsTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            velocityY = -9;
            if(gameOver) {
                // reset
                alen.y = alenY;
                velocityY = 0;
                buildings.clear();
                score= 0;
                gameOver = false;
                gameLoop.start();
                placeBuildingsTimer.start();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
