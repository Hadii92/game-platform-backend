package sk.tuke.gamestudio.game;

public enum BlockType {
    O(new int[][]{{1, 1}, {1, 1}}, "\u001B[31m"),       // red
    I(new int[][]{{1}, {1}, {1}}, "\u001B[34m"),        // blue
    S(new int[][]{{0, 1, 1}, {1, 1, 0}}, "\u001B[32m"), // green
    Z(new int[][]{{1, 1, 0}, {0, 1, 1}}, "\u001B[33m"), // yellow
    T(new int[][]{{1, 1, 1}, {0, 1, 0}}, "\u001B[35m"), // purple
    L(new int[][]{{1, 1, 1}, {1, 0, 0}}, "\u001B[36m"), // cyan
    J(new int[][]{{1, 1, 1}, {0, 0, 1}}, "\u001B[37m"); // white
    private final int[][] shape;
    private final String color;
    BlockType(int[][] shape, String color) {
        this.shape = shape;
        this.color = color;
    }
    public int[][] getShape() {
        return shape;
    }
    public String getColor() {
        return color;
    }
}
