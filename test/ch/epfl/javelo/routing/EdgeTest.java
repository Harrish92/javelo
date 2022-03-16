package ch.epfl.javelo.routing;

import org.junit.jupiter.api.Test;

import java.util.function.DoubleUnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EdgeTest implements DoubleUnaryOperator {

    @Test
    public void elevationAtTest(){
        Edge e = new Edge(0, 0, null, null, 0, new EdgeTest());
        double position = 2;
        double actual = e.elevationAt(position);
        double expected = 4;
        assertEquals(expected, actual);
    }


    @Override
    public double applyAsDouble(double x) {
        return x*x;
    }
}
