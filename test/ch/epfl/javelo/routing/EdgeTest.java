package ch.epfl.javelo.routing;

import ch.epfl.javelo.data.Graph;
import ch.epfl.javelo.data.GraphEdges;
import ch.epfl.javelo.data.GraphNodes;
import ch.epfl.javelo.projection.PointCh;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EdgeTest {

    private static final double SWISS_MIN_E = 2_485_000;
    private static final double SWISS_MIN_N = 1_075_000;
    private static final double SWISS_WIDTH = 349_000;
    private static final double SWISS_HEIGHT = 221_000;

    private static final int SUBDIVISIONS_PER_SIDE = 128;
    private static final int SECTORS_COUNT = SUBDIVISIONS_PER_SIDE * SUBDIVISIONS_PER_SIDE;
    private static final double SECTOR_WIDTH = SWISS_WIDTH / SUBDIVISIONS_PER_SIDE;
    private static final double SECTOR_HEIGHT = SWISS_HEIGHT / SUBDIVISIONS_PER_SIDE;

    @Test
    public void PositionClosestToWorks(){
        /*ByteBuffer edgesBuffer = ByteBuffer.allocate(10);
        // Sens : inversé. Nœud destination : 12.
        edgesBuffer.putInt(0, ~12);
        // Longueur : 0x10.b m (= 16.6875 m)
        edgesBuffer.putShort(4, (short) 0x10_b);
        // Dénivelé : 0x10.0 m (= 16.0 m)
        edgesBuffer.putShort(6, (short) 0x10_0);
        // Identité de l'ensemble d'attributs OSM : 1
        edgesBuffer.putShort(8, (short) 2022);

        IntBuffer profileIds = IntBuffer.wrap(new int[]{
                // Type : 3. Index du premier échantillon : 1.
                (3 << 30) | 1
        });

        ShortBuffer elevations = ShortBuffer.wrap(new short[]{
                (short) 0,
                (short) 0x180C, (short) 0xFEFF, (short) 0xFFFE, (short) 0xF000
        });

        GraphEdges edges = new GraphEdges(edgesBuffer, profileIds, elevations);


        ByteBuffer sectorsBuffer = ByteBuffer.allocate(SECTORS_COUNT * (Integer.BYTES + Short.BYTES));
        for (int i = 0; i < SECTORS_COUNT; i += 1) {
            sectorsBuffer.putInt(i);
            sectorsBuffer.putShort((short) 1);
        }
        assert !sectorsBuffer.hasRemaining();
        ByteBuffer sector = sectorsBuffer.rewind().asReadOnlyBuffer();

        Graph graph = new Graph(, sector, edges);
        int fromId = 35295150;
        int toNodeId = 1682248;
        PointCh fromPoint = graph.nodePoint(fromId);
        PointCh toPoint = graph.nodePoint(toNodeId);
        Edge edge = new Edge(fromId, toNodeId, );

         */
    }
    @Test
    public void positionClosestTo(){

    }

    @Test
    public void pointAtTest(){

    }
}
