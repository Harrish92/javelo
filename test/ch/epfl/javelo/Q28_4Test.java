package ch.epfl.javelo;

import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

public class Q28_4Test {

    @Test
    public void ofIntTest(){
        int actual = Q28_4.ofInt(128);
        int expected = 2048;

        assertEquals(expected, actual);

    }

    @Test
    public void asDoubleTest(){
        double actual = Q28_4.asDouble(2048);
        double expected = 128;
        assertEquals(expected, actual);
    }

    @Test
    public void asFloatTest(){
        float actual  = Q28_4.asFloat(12);
        float expected = 0.75f;
        assertEquals(expected, actual);

    }
}
