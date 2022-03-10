package ch.epfl.javelo.data;

import ch.epfl.javelo.projection.PointCh;
import ch.epfl.javelo.projection.SwissBounds;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GraphSectorsTest {

    private record Sector(int startNodeId, int endNodeId) {}

    @Test
    void sectorTest() {
        byte[] a = new byte[128*128*6];
        ByteBuffer b = ByteBuffer.wrap(a);
        b.put(0, new byte[] {(byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 11});
        b.put(6, new byte[] {(byte) 0, (byte) 0, (byte) 0, (byte) 13, (byte) 0, (byte) 100});
        b.put(12, new byte[] {(byte) 0, (byte) 0, (byte) 0, (byte) 123, (byte) 0, (byte) 0});
        b.put(18, new byte[] {(byte) 0, (byte) 0, (byte) 0, (byte) 56, (byte) 1, (byte) 0});
        b.put(129*6, new byte[] {(byte) 1, (byte) 35, (byte) 1, (byte) 1, (byte) 9, (byte) 87});
        b.put(130*6, new byte[] {(byte) 2, (byte) 0, (byte) 0, (byte) 0, (byte) 23, (byte) 24});
        GraphSectors s = new GraphSectors(b);
        List<Sector> liste = new ArrayList<>();
        liste.add(new Sector(1,12));
        liste.add(new Sector(13,113));
        liste.add(new Sector(19071233,19073624));
        int d = 2000;
        assertArrayEquals(liste.toArray(), s.sectorsInArea(new PointCh(SwissBounds.MIN_E+d, SwissBounds.MIN_N+d), d).toArray());

        //TODO: coord négatives ?
        //TODO: assertequals pour sector

    }
}