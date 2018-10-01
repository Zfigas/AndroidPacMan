package org.example.pacman;

public class Pacman {

    public int pacX;
    public int pacY;


    public Pacman(int x, int y) {
        this.pacX = x;
        this.pacY = y;
    }

    public void moveRight(int x) {
        this.pacX = pacX + x;
    }

    public void moveLeft(int x) {
        this.pacX = pacX - x;
    }

    public void moveTop(int y) {
        this.pacY = pacY - y;
    }

    public void moveBottom(int y) {
        this.pacY = pacY + y;
    }
}