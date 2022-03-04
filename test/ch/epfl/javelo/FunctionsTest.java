package ch.epfl.javelo;

import ch.epfl.javelo.projection.PointWebMercator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class FunctionsTest {

    @Test
    void constantTest(){
        assertEquals(7, Functions.constant(7).applyAsDouble(-2));
        assertEquals(-3, Functions.constant(-3).applyAsDouble(5));
    }

    @Test
    void sampledErrorTest(){
        float[] tab = {};
        float[] tab2 = new float[]{2, -5};
        float[] tab1 = new float[]{4};
        double x=3;
        assertThrows(IllegalArgumentException.class, () -> Functions.sampled(tab, x));
        assertThrows(IllegalArgumentException.class, () -> Functions.sampled(tab1, x));
        assertThrows(IllegalArgumentException.class, () -> Functions.sampled(tab2, 0));
        assertThrows(IllegalArgumentException.class, () -> Functions.sampled(tab2, -3));
    }

    @Test
    void sampledValuesTest(){
        float[] tab = new float[]{-1, 2, 3, 3.5F, -2};
        double xMax = 4;
        assertEquals(-1, Functions.sampled(tab, xMax).applyAsDouble(-5));
        assertEquals(2, Functions.sampled(tab, xMax).applyAsDouble(1));
        assertEquals(-1, Functions.sampled(tab, xMax).applyAsDouble(0));
        assertEquals(3.5, Functions.sampled(tab, xMax).applyAsDouble(3));
        assertEquals(-2, Functions.sampled(tab, xMax).applyAsDouble(4));
        assertEquals(-2, Functions.sampled(tab, xMax).applyAsDouble(7));
        assertEquals(3, Functions.sampled(tab, xMax).applyAsDouble(2));
        assertEquals(0, Functions.sampled(tab, xMax).applyAsDouble(0.333), 0.001);
        assertEquals(2.5, Functions.sampled(tab, xMax).applyAsDouble(1.5));
        xMax = 20;
        assertEquals(2.5, Functions.sampled(tab, xMax).applyAsDouble(7.5));
        assertEquals(3.5, Functions.sampled(tab, xMax).applyAsDouble(15));
    }
}