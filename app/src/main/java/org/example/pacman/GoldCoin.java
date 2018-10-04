package org.example.pacman;

import java.util.Random;

public class GoldCoin {
        public int goldX;
        public int goldY;
        private Random rand = new Random();

        public GoldCoin(int w, int h) {
            //size of coin 80 so to not let it be spawned in wall we need to remove half
            this.goldX = rand.nextInt(w - 40);
            this.goldY = rand.nextInt(h - 40);
        }
    }

