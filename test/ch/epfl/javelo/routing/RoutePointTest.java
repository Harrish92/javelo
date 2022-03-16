package ch.epfl.javelo.routing;

import ch.epfl.javelo.projection.PointCh;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class RoutePointTest {

    @Test
    public void withPositionShiftedByTest(){
        PointCh pch = new PointCh(2485000, 1075000);
        double position = 8;
        double ditanceToReference = 20;
        double positionDifference = 5;
        RoutePoint rtp = new RoutePoint(pch, position, ditanceToReference);
        RoutePoint expected = new RoutePoint(pch, 13, ditanceToReference);
        RoutePoint actual = rtp.withPositionShiftedBy(positionDifference);
        assertEquals(expected, actual);
    }

    @Test
    public void minTest1(){
        PointCh pch = new PointCh(2485000, 1075000);
        double position = 8;
        double maxDitanceToReference = 20;
        double minDitanceToReference = 5;
        RoutePoint expected = new RoutePoint(pch, position, minDitanceToReference);
        RoutePoint actual = new RoutePoint(pch, position, maxDitanceToReference).min(expected);
        assertEquals(expected, actual);



    }

    @Test
    public void minTest2(){
        PointCh pch = new PointCh(2485000, 1075000);
        double position = 8;
        double maxDitanceToReference = 20;
        double minDitanceToReference = 5;
        RoutePoint expected = new RoutePoint(pch, position, minDitanceToReference);
        RoutePoint actual = new RoutePoint(pch, position, maxDitanceToReference).min(pch, position, minDitanceToReference);
        assertEquals(expected, actual);

    }
}
