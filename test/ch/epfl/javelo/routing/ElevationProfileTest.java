package ch.epfl.javelo.routing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ElevationProfileTest {

    @Test
    void contructorTest() {
        final float[] a = {0};
        assertThrows(IllegalArgumentException.class, () -> new ElevationProfile(1, a));
        assertThrows(IllegalArgumentException.class, () -> new ElevationProfile(0, a));
        final float[] b = new float[]{1, 2};
        assertThrows(IllegalArgumentException.class, () -> new ElevationProfile(0, b));
        assertThrows(IllegalArgumentException.class, () -> new ElevationProfile(-0.1, b));
    }

    @Test
    void valuesTest() {
        final float[] b = new float[]{1, 2, 3};
        ElevationProfile e = new ElevationProfile(2, b);
        assertEquals(2, e.length());
        assertEquals(2.5, e.elevationAt(1.5));
        assertEquals(3, e.elevationAt(10));
        assertEquals(1, e.elevationAt(-5));
        final float[] a = new float[]{1, 2, 0, 3, 5, -1};
        ElevationProfile e2 = new ElevationProfile(10, a);
        assertEquals(6, e2.totalAscent());
        assertEquals(8, e2.totalDescent());
        assertEquals(5, e2.maxElevation());
        assertEquals(-1, e2.minElevation());
        final float[] c = new float[]{0.1f, 0.3f, 0.5f};
        e = new ElevationProfile(2, c);
        assertEquals(0.5f, e.maxElevation(), 0.0001);
        assertEquals(0.1f, e.minElevation(), 0.0001);
    }
}
