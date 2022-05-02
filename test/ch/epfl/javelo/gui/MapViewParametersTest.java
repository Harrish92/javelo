package ch.epfl.javelo.gui;

import ch.epfl.javelo.projection.PointWebMercator;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MapViewParametersTest {
    @Test
    void pointAtTest(){
        int x = 135735;
        int y = 92327;
        PointWebMercator p = PointWebMercator.of(10, x, y);
        MapViewParameters m  = new MapViewParameters(10, x, y);
        assertEquals(p.x(), m.pointAt(0,0).x());
        assertEquals(p.y(), m.pointAt(0,0).y());
    }

    @Test
    void viewXYTest(){
        int x = 135735;
        int y = 92327;
        PointWebMercator p = PointWebMercator.of(10, x+1, y+1);
        MapViewParameters m  = new MapViewParameters(10, x, y);
        assertEquals(1, m.viewX(p));
        assertEquals(1, m.viewY(p));
    }

    @Test
    void topLeftTest(){

    }

}