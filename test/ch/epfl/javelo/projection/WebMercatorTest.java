package ch.epfl.javelo.projection;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WebMercatorTest {
    private static final double DELTA = 1e-7;

    @Test
    public void xWorksOnKnownValue(){
        var actual1 = WebMercator.x(Math.toRadians(47));
        var expected1 = 0.630555556;
        assertEquals(expected1, actual1, DELTA);


    }

    @Test
    public void yWorksOnKnownValue(){
        var actual1 = WebMercator.y(Math.toRadians(47));
        var expected1 = 0.351726223;
        assertEquals(expected1, actual1, DELTA);

    }

    @Test
    public void lonWorksOnKnownValue(){
        var actual1 = WebMercator.lon(100);
        var expected1 = 625.1769381;
        assertEquals(expected1, actual1, DELTA);

    }

    @Test
    public void latWorksOnKnownValue(){
        var actual1 = WebMercator.lat(25);
        var expected1 = -1.570796327;
        assertEquals(expected1, actual1, DELTA);

    }


}