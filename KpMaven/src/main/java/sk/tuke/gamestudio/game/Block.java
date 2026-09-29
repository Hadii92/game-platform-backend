package sk.tuke.gamestudio.game;

import java.util.Random;

public class Block {
    private int[][] shape;
    private String color;

    public Block(int[][] shape, String color) {
        this.shape = shape;
        this.color = color;
    }

    public int[][] getShape() {
        return shape;
    }

    public String getColor() {
        return color;
    }

    public static Block getRandomBlock() {
        int[][][] shapes = {
                {{1, 1}, {1, 1}},        // O
                {{1}, {1}, {1}},         // I
                {{0, 1, 1}, {1, 1, 0}}, // S
                {{1, 1, 0}, {0, 1, 1}}, // Z
                {{1, 1, 1}, {0, 1, 0}}, // T
                {{1, 1, 1}, {1, 0, 0}}, // L
                {{1, 1, 1}, {0, 0, 1}}  // J
        };
        String[] colors = {
                "#FF6B6B", // O — мягкий красный
                "#4D96FF", // I — чистый голубой
                "#1DD1A1", // S — свежий зелёный
                "#FDCB6E", // Z — тёплый жёлтый
                "#A29BFE", // T — светлый сиреневый
                "#FFA36C", // L — мягкий оранжевый
                "#6C5CE7"  // J — насыщенный синий-фиолетовый
        };
        Random rand = new Random();
        int shapeIndex = rand.nextInt(shapes.length);
        return new Block(shapes[shapeIndex], colors[shapeIndex]);
    }
}