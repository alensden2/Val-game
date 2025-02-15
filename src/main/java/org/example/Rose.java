package org.example;

import java.awt.*;

public class Rose {
    int x;
    int y;
    int width;
    int height;
    Image img;
    boolean collected = false;
    Rose(int x,int y, int width, int height, Image img){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.img = img;
    }
}
