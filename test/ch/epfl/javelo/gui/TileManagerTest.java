package ch.epfl.javelo.gui;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;


public class TileManagerTest {

    @Test
    public void FileIsCreated() throws IOException {
        TileManager m =  new TileManager(Path.of("cache") ,
                "https://tile.openstreetmap.org");
        TileManager.TileId tId = new TileManager.TileId(19, 271725, 185422);
        m.imageForTileAt(tId);
    }

    @Test
    public void isValidTest(){
        assertFalse(TileManager.TileId.isValid(0, 1, 1));
        assertTrue(TileManager.TileId.isValid(0, 0, 0));
        assertTrue(TileManager.TileId.isValid(1, 1, 1));
        assertThrows(IllegalArgumentException.class, ()->{
            new TileManager.TileId(0, 1, 1);
        });
    }

}
