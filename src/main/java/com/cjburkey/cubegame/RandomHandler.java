package com.cjburkey.cubegame;

import java.util.Random;

public class RandomHandler {
    
    private static RandomHandler mainInstance;
    
    private Random random;
    
    public RandomHandler() {
        mainInstance = this;
        random = new Random();
    }
    
    public Random getRandom() {
        return random;
    }
    
    public int betweenIncI(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
    
    public int betweenExcI(int min, int max) {
        return random.nextInt(max - min) + min;
    }
    
    public float betweenExcF(float min, float max) {
        return random.nextFloat() * (max - min) + min;
    }
    
    public static RandomHandler getMainInstance() {
        return mainInstance;
    }
    
}