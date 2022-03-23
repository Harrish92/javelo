package ch.epfl.javelo.routing;

import ch.epfl.javelo.projection.PointCh;
import org.junit.jupiter.api.Test;

import java.awt.geom.QuadCurve2D;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SingleRouteTest {

    @Test
    public void lengthTest(){
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0, 1, new PointCh(2485000, 1075000), new PointCh(2485000, 1075000), 1, operand -> operand*operand));
        edges.add(new Edge(0, 1, new PointCh(2485000, 1075000), new PointCh(2485000, 1075000), 2, operand -> operand*operand));
        SingleRoute sr = new SingleRoute(edges);
        double expected = 3;
        double actual = sr.length();
        assertEquals(expected, actual);


    }
}
