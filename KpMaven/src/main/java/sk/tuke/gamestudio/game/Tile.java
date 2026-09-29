package sk.tuke.gamestudio.game;

public class Tile {
    private TileState state;
    private String color;
    public Tile() {
        this.state = TileState.FREE;
        this.color = null;
    }
    public TileState getState() {
        return state;
    }
    public void setState(TileState state) {
        this.state = state;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }

}