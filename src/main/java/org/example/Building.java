package org.example;

import java.awt.*;

public class Building {
    int x;
    int y;
    int width;
    int height;
    Image img;
    boolean passed = false;

    Building(int x,int y, int width, int height, Image img){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.img = img;
        this.passed = passed;
    }

}
