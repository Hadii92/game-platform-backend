package sk.tuke.gamestudio.game;




public class Field {
    private int rows, cols;
    private Tile[][] tiles;
    private int score;
    public Field(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.tiles = new Tile[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                tiles[i][j] = new Tile();
            }
        }
    }
    public int getScore() {
        return score;
    }
    public Tile[][] getTiles() {
        return this.tiles;
    }
    public int getCols() {
        return cols;
    }
    public GameState getGameState() {
        return hasSpaceForBlock(Block.getRandomBlock()) ? GameState.PLAYING : GameState.FAILED;
    }
    public boolean canPlaceBlock(int row, int col, Block block) {
        int[][] shape = block.getShape();
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] == 1) {
                    int r = row + i, c = col + j;
                    if (r >= rows || c >= cols || tiles[r][c].getState() == TileState.OCCUPIED)
                        return false;
                }
            }
        }
        return true;
    }
    public void placeBlock(int row, int col, Block block) {
        int[][] shape = block.getShape();
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] == 1) {
                    tiles[row + i][col + j].setState(TileState.OCCUPIED);
                    tiles[row + i][col + j].setColor(block.getColor()); // Устанавливаем цвет блока
                }
            }
        }
        clearFullLines();
    }
    private void clearFullLines() {
        for (int i = 0; i < rows; i++) {
            boolean fullRow = true;
            for (int j = 0; j < cols; j++) {
                if (tiles[i][j].getState() == TileState.FREE) {
                    fullRow = false;
                    break;
                }
            }
            if (fullRow) {
                for (int j = 0; j < cols; j++) {
                    tiles[i][j].setState(TileState.FREE);
                    tiles[i][j].setColor(null);
                }
                score += 10;
            }
        }
        for (int j = 0; j < cols; j++) {
            boolean fullColumn = true;
            for (int i = 0; i < rows; i++) {
                if (tiles[i][j].getState() == TileState.FREE) {
                    fullColumn = false;
                    break;
                }
            }
            if (fullColumn) {
                for (int i = 0; i < rows; i++) {
                    tiles[i][j].setState(TileState.FREE);
                    tiles[i][j].setColor(null);
                }
                score += 10;
            }
        }
    }
    public boolean hasSpaceForBlock(Block block) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (canPlaceBlock(i, j, block)) {
                    return true;
                }
            }
        }
        return false;
    }
    public void display() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (tiles[i][j].getState() == TileState.OCCUPIED) {
                    System.out.print(tiles[i][j].getColor() + "■ " + "\u001B[0m");
                } else {
                    System.out.print("· ");
                }
            }
            System.out.println();
        }
        System.out.println("Record: " + score);
    }
}