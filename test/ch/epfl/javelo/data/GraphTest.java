package ch.epfl.javelo.data;

import ch.epfl.javelo.projection.Ch1903;
import ch.epfl.javelo.projection.PointCh;
import ch.epfl.javelo.projection.SwissBounds;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GraphTest {

    @Test
    void loadFromThrowsTest() {
        Path path = Path.of("chemin_invalide");
        assertThrows(IOException.class, () -> Graph.loadFrom(path));
        //assertThrows(IOException.class, () -> Graph.loadFrom(null));
    }

    /*@Test
    void loadFromValuesTest() throws IOException {
        Graph graph = Graph.loadFrom(Path.of("lausanne"));
        try (FileChannel channel1 = FileChannel.open(Path.of("lausanne").resolve("attributes.bin"));
            FileChannel channel2 = FileChannel.open(Path.of("lausanne").resolve("edges.bin"));
            FileChannel channel3 = FileChannel.open(Path.of("lausanne").resolve("elevations.bin"));
             FileChannel channel4 = FileChannel.open(Path.of("lausanne").resolve("nodes.bin"));
             FileChannel channel5 = FileChannel.open(Path.of("lausanne").resolve("profile_ids.bin"));
             FileChannel channel6 = FileChannel.open(Path.of("lausanne").resolve("sectors.bin"));
        ) {
            ArrayList<AttributeSet> a = new ArrayList<>();
            MappedByteBuffer buffer = channel1.map(FileChannel.MapMode.READ_ONLY, 0, channel1.size());
            for(int i = 0; i < buffer.asLongBuffer().limit(); i++){
                a.add(new AttributeSet(buffer.asLongBuffer().get(i)));
            }
            GraphNodes nodes = new GraphNodes(channel4.map(FileChannel.MapMode.READ_ONLY,0,channel4.size()).asIntBuffer());
            GraphSectors sectors = new GraphSectors(channel6.map(FileChannel.MapMode.READ_ONLY, 0, channel6.size()));
            GraphEdges edges = new GraphEdges(channel2.map(FileChannel.MapMode.READ_ONLY, 0, channel2.size()),
                    channel5.map(FileChannel.MapMode.READ_ONLY, 0, channel5.size()).asIntBuffer(),
                    channel3.map(FileChannel.MapMode.READ_WRITE, 0, channel3.size()).asShortBuffer());
            assertEquals(new Graph(nodes, sectors, edges, a), graph);
        }
    }*/

    @Test
    void nodeClosestToTest() throws IOException {
        Graph graph = Graph.loadFrom(Path.of("lausanne"));
        /*assertEquals(310876657, graph.nodeClosestTo(new PointCh(Ch1903.e(Math.toRadians(6.60161),Math.toRadians(46.63271)),
                Ch1903.n(Math.toRadians(6.60161), Math.toRadians(46.63271))), 0));*/
        assertEquals(-1, graph.nodeClosestTo(new PointCh(Ch1903.e(Math.toRadians(6.60161),Math.toRadians(46.63271)),
                Ch1903.n(Math.toRadians(6.60161), Math.toRadians(46.63271))), 0));
        assertEquals(2022, graph.nodeClosestTo(new PointCh(Ch1903.e(Math.toRadians( 6.6013034),Math.toRadians( 46.6326106)),
                Ch1903.n(Math.toRadians( 6.6013034), Math.toRadians( 46.6326106))), 1));
    }
}
