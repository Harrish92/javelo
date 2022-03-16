package ch.epfl.javelo.data;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class GraphTest {

    @Test
    void loadFromThrowsTest() {
        Path path = Path.of("chemin_invalide");
        assertThrows(IOException.class, () -> Graph.loadFrom(path));
        assertThrows(IOException.class, () -> Graph.loadFrom(null));
    }

    @Test
    void loadFromValuesTest() throws IOException {
        Graph graph = Graph.loadFrom(Path.of("lausanne"));


    }

    @Test
    void nodeClosestToTest() {

    }
}
