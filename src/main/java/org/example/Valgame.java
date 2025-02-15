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
    Image krishnaImg;

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
    Timer placeRoseTimer;
    double score = 0;

    List<Building> buildings;
    List<Rose> roses;

    // Rose
    int roseX = boardWidth;
    int roseWidth = 70;
    int roseHeight = 70;
    int roseY = boardHeight - roseHeight;

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
        krishnaImg = new ImageIcon(getClass().getResource("./Krishna.png")).getImage();
        alen = new Alen(alenX,alenY,alenWidth,alenHeight,alenImg);
        gameLoop = new Timer(1000/60, this);
        buildings = new ArrayList<Building>();
        roses = new ArrayList<Rose>();
        placeBuildingsTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeBuildings();
            }
        });
        placeRoseTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeRoses();
            }
        });
        placeBuildingsTimer.start();
        placeRoseTimer.start();
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

    public void placeRoses() {
        int roseWidth = 60;
        int roseHeight = 90;
        int maxAttempts = 10;
        int minSpacing = 150; // Ensures roses are spread out horizontally

        for (int i = 0; i < 3; i++) {
            boolean placed = false;
            int attempts = 0;
            while (!placed && attempts < maxAttempts) {
                int randomX = boardWidth + (i * minSpacing) + (int) (Math.random() * 100); // Random offset for better spread

                // Find the highest building within this X range
                int highestBuildingY = boardHeight; // Default if no buildings
                for (Building building : buildings) {
                    if (randomX > building.x - 50 && randomX < building.x + building.width + 50) {
                        highestBuildingY = Math.min(highestBuildingY, building.y);
                    }
                }

                // If no building nearby, place it in a general range
                if (highestBuildingY == boardHeight) {
                    highestBuildingY = boardHeight - 250; // Default height when no building is close
                }

                // Ensure rose is **above** the building but not too high
                int minRoseY = highestBuildingY - roseHeight - 20; // Slightly above the building
                int maxRoseY = highestBuildingY - roseHeight - 50; // Extra spacing
                int randomY = Math.max(minRoseY, maxRoseY - (int) (Math.random() * 30));

                // Ensure roses do not overlap each other
                Rose newRose = new Rose(randomX, randomY, roseWidth, roseHeight, rose);
                boolean overlaps = false;
                for (Rose existingRose : roses) {
                    if (randomX < existingRose.x + existingRose.width + 10 &&
                            randomX + roseWidth > existingRose.x - 10 &&
                            randomY < existingRose.y + existingRose.height &&
                            randomY + roseHeight > existingRose.y) {
                        overlaps = true;
                        break;
                    }
                }

                if (!overlaps) {
                    roses.add(newRose);
                    placed = true;
                }
                attempts++;
            }
        }
    }


    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void move() {
        if (score >= 150) {
            gameOver = true; // Stop game when score reaches 150
            return;
        }

        velocityY += gravity;
        alen.y += velocityY;
        alen.y = Math.max(alen.y, 0);

        // Move buildings
        for (int i = 0; i < buildings.size(); i++) {
            Building building = buildings.get(i);
            building.x += velocityX;

            if (!building.passed && alen.x > building.x + building.width) {
                score += 1.0;
                building.passed = true;
            }
        }

        // Move and check collision with roses
        for (int i = 0; i < roses.size(); i++) {
            Rose rose = roses.get(i);
            rose.x += velocityX;

            if (collisionRoses(alen, rose)) {
                score += 5; // Increase score for collecting a rose
                roses.remove(i);
                i--; // Adjust index after removal
            }
        }

        // Game over condition
        if (alen.y > boardHeight) {
            gameOver = true;
        }
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, boardWidth, boardHeight, null);
        g.drawImage(alenImg, alen.x, alen.y, alen.width, alen.height, null);

        // Draw buildings
        for (Building building : buildings) {
            g.drawImage(building.img, building.x, building.y, building.width, building.height, null);
            if (collision(alen, building)) {
                gameOver = true;
            }
        }

        // Draw roses
        for (Rose rose : roses) {
            g.drawImage(rose.img, rose.x, rose.y, rose.width, rose.height, null);
        }
        FontMetrics metrics = g.getFontMetrics(); // Get font size details

        // Display Score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        g.drawString("Score: " + (int) score, 10, 35);

        if (gameOver) {
            String message = (score >= 150) ? "YOU WON! 🎉" : "Game Over!";
            int startX = boardWidth - metrics.stringWidth(message) - 350; // Align to right
            g.drawString(message, startX, 40);
            g.drawString("Press Spacebar to Restart", startX, 100);
        }
    }


    public boolean collision(Alen a, Building b){
        return a.x < b.x + b.width &&   //a's top left corner doesn't reach b's top right corner
                a.x + a.width > b.x &&   //a's top right corner passes b's top left corner
                a.y < b.y + b.height &&  //a's top left corner doesn't reach b's bottom left corner
                a.y + a.height > b.y;
    }
    public boolean collisionRoses(Alen a, Rose b){
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
        }
        if(gameOver && e.getKeyCode() == KeyEvent.VK_SHIFT) {
            // reset
            alen.y = alenY;
            velocityY = 0;
            buildings.clear();
            score= 0;
            gameOver = false;
            gameLoop.start();
            placeBuildingsTimer.start();
            placeRoseTimer.start();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
