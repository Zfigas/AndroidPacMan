package org.example.pacman;

import java.util.Random;

/**
 * This class should contain information about a single GoldCoin.
 * such as x and y coordinates (int) and whether or not the goldcoin
 * has been taken (boolean)
 */

public class GoldCoin {


        public final static int radius = 20;
        public int x;
        public int y;
        private Random rand = new Random();

        public GoldCoin(int w, int h) {
            this.x = rand.nextInt(w - 2*radius) + radius;
            this.y = rand.nextInt(h - 2*radius) + radius;
        }
    }

