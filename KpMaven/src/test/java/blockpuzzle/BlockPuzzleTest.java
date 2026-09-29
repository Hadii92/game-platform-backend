package blockpuzzle;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.game.*;

public class BlockPuzzleTest {
    private Field field;
    private Block block;
    @BeforeEach
    void setUp() {
        field = new Field(10, 10);
    }
    @Test
    void testFieldInitialization() {
        Tile[][] tiles = field.getTiles();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                assertEquals(TileState.FREE, tiles[i][j].getState(),
                        "all tiles must be free after init");
            }
        }
    }

    @Test
    void testBlockPlacementAndTileStateChange() {
        assertTrue(field.canPlaceBlock(5, 5, block), "Block should be placed");
        field.placeBlock(5, 5, block);
        Tile[][] tiles = field.getTiles();
        assertEquals(TileState.OCCUPIED, tiles[5][5].getState(), "Tile must be occupied");
        assertEquals(TileState.OCCUPIED, tiles[5][6].getState(), "Tile must be occupied");
        assertEquals(TileState.OCCUPIED, tiles[6][5].getState(), "Tile must be occupied");
        assertEquals(TileState.OCCUPIED, tiles[6][6].getState(), "Tile must be occupied");
    }
    @Test
    void testRandomBlockGeneration() {
        Block block1 = Block.getRandomBlock();
        Block block2 = Block.getRandomBlock();
        assertNotNull(block1, "Block must not be null");
        assertNotNull(block2, "Block must not be null");
        assertNotEquals(block1, block2, "The figures must be different at least sometimes");
    }
    @Test
    void testGameStateAfterPlayerMove() {
        assertTrue(field.canPlaceBlock(4, 4, block), "Block should be placed");
        field.placeBlock(4, 4, block);
        assertEquals(GameState.PLAYING, field.getGameState(), "The game have to continue after succes move  ");
    }
}