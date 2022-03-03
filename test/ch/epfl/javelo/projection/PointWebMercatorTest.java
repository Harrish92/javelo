package ch.epfl.javelo.projection;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointWebMercatorTest {

    @Test
    void errorTest(){
        assertThrows(IllegalArgumentException.class, () -> new PointWebMercator(0,-0.1));
        assertThrows(IllegalArgumentException.class, () -> new PointWebMercator(-0.2,0));
        assertThrows(IllegalArgumentException.class, () -> new PointWebMercator(-0.3,-0.4));
        assertThrows(IllegalArgumentException.class, () -> new PointWebMercator(1,1.01));
        assertThrows(IllegalArgumentException.class, () -> new PointWebMercator(1.2,1));
        assertThrows(IllegalArgumentException.class, () -> new PointWebMercator(2,-0.1));
    }



}