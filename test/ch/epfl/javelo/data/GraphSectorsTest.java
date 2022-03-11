package ch.epfl.javelo.data;

import ch.epfl.javelo.projection.PointCh;
import ch.epfl.javelo.projection.SwissBounds;
import ch.epfl.javelo.data.GraphSectors.Sector;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class GraphSectorsTest {



    @Test
    void sectorTest() {
        byte[] a = new byte[128*128*6];
        ByteBuffer b = ByteBuffer.wrap(a);
        b.put(0, new byte[] {(byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 11});
        b.put(6, new byte[] {(byte) 0, (byte) 0, (byte) 0, (byte) 13, (byte) 0, (byte) 100});
        b.put(12, new byte[] {(byte) 0, (byte) 0, (byte) 0, (byte) 123, (byte) 0, (byte) 0});
        b.put(18, new byte[] {(byte) 0, (byte) 0, (byte) 0, (byte) 56, (byte) 1, (byte) 0});
        b.put(128*6, new byte[] {(byte) 0, (byte) 0, (byte) 0, (byte) 88, (byte) 0, (byte) 87});
        b.put(129*6, new byte[] {(byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 24});
        b.put(128*127*6, new byte[] {(byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 11});
        GraphSectors s = new GraphSectors(b);
        List<Sector> liste = new ArrayList<>();
        liste.add(new Sector(1,12));
        liste.add(new Sector(13,113));
        liste.add(new Sector(88, 175));
        liste.add(new Sector(256, 280));
        int d = 2000;
        assertThrows(IllegalArgumentException.class,() -> s.sectorsInArea(new PointCh(SwissBounds.MIN_E, SwissBounds.MIN_N), -1).get(0));
        for(int i = 0; i < liste.size(); i++) {
            assertEquals(liste.get(i), s.sectorsInArea(new PointCh(SwissBounds.MIN_E + 2726.5625, SwissBounds.MIN_N + 1726.5625), 1).get(i));
        }
        for(int i = 0; i < liste.size(); i++) {
            assertEquals(liste.get(i), s.sectorsInArea(new PointCh(SwissBounds.MIN_E, SwissBounds.MIN_N), 3000).get(i));
        }
    }
}