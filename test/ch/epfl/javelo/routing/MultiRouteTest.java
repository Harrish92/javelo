package ch.epfl.javelo.routing;

import ch.epfl.javelo.projection.PointCh;
import org.junit.jupiter.api.Test;

import static ch.epfl.test.TestRandomizer.RANDOM_ITERATIONS;
import static ch.epfl.test.TestRandomizer.newRandom;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultiRouteTest {
    public final static double MIN_E = 2485000;
    public final static double MIN_N = 1075000;

    private static final int ORIGIN_N = 1_200_000;
    private static final int ORIGIN_E = 2_600_000;
    private static final double EDGE_LENGTH = 100.25;

    // Sides of triangle used for "sawtooth" edges (shape: /\/\/\…)
    private static final double TOOTH_EW = 1023;
    private static final double TOOTH_NS = 64;
    private static final double TOOTH_LENGTH = 1025;
    private static final double TOOTH_ELEVATION_GAIN = 100d;
    private static final double TOOTH_SLOPE = TOOTH_ELEVATION_GAIN / TOOTH_LENGTH;


    @Test
    public void lengthTest(){
        List<Route> lr = new ArrayList<>();
        List<Edge> le1 = new ArrayList<>();
        List<Edge> le2 = new ArrayList<>();

        le1.add(new Edge(0, 1, new PointCh(MIN_E , MIN_N ), new PointCh(MIN_E + 10, MIN_N),
                10, operand -> operand));
        le1.add(new Edge(1, 2, new PointCh(MIN_E+10, MIN_N), new PointCh(MIN_E + 20, MIN_N),
                10, operand -> operand));
        lr.add(new SingleRoute(le1));

        le2.add(new Edge(1, 2, new PointCh(MIN_E+10, MIN_N), new PointCh(MIN_E + 20, MIN_N ),
                10, operand -> operand));
        le2.add(new Edge(3, 4, new PointCh(MIN_E+20, MIN_N), new PointCh(MIN_E + 28, MIN_N),
                8, operand -> operand));
        lr.add(new SingleRoute(le2));

        MultiRoute mr = new MultiRoute(lr);

        double expected = 38;
        double actual = mr.length();
        assertEquals(expected, actual);
    }

    @Test
    public void edgesTest(){
        List<Route> lr = new ArrayList<>();
        List<Edge> le1 = new ArrayList<>();
        List<Edge> le2 = new ArrayList<>();

        Edge e1 = new Edge(0, 1, new PointCh(MIN_E , MIN_N ), new PointCh(MIN_E + 10, MIN_N),
                10, operand -> operand);
        Edge e2 = new Edge(1, 2, new PointCh(MIN_E+10, MIN_N), new PointCh(MIN_E + 20, MIN_N),
                10, operand -> operand);
        Edge e3 = new Edge(1, 2, new PointCh(MIN_E+10, MIN_N), new PointCh(MIN_E + 20, MIN_N ),
                10, operand -> operand);
        Edge e4 = new Edge(3, 4, new PointCh(MIN_E+20, MIN_N), new PointCh(MIN_E + 28, MIN_N),
                8, operand -> operand);

        le1.add(e1);
        le1.add(e2);
        lr.add(new SingleRoute(le1));

        le2.add(e3);
        le2.add(e4);

        lr.add(new SingleRoute(le2));

        MultiRoute mr = new MultiRoute(lr);
        List<Edge> expected = new ArrayList<>();

        expected.add(e1);
        expected.add(e2);
        expected.add(e3);
        expected.add(e4);

        List<Edge> actual = mr.edges();
        assertEquals(expected, actual);
    }

    @Test
    public void pointsTest(){
        List<Route> lr = new ArrayList<>();
        List<Edge> le1 = new ArrayList<>();
        List<Edge> le2 = new ArrayList<>();

        Edge e1 = new Edge(0, 1, new PointCh(MIN_E , MIN_N ), new PointCh(MIN_E + 10, MIN_N),
                10, operand -> operand);
        Edge e2 = new Edge(1, 2, new PointCh(MIN_E+10, MIN_N), new PointCh(MIN_E + 20, MIN_N),
                10, operand -> operand);

        Edge e3 = new Edge(1, 2, new PointCh(MIN_E+10, MIN_N), new PointCh(MIN_E + 20, MIN_N ),
                10, operand -> operand);
        Edge e4 = new Edge(3, 4, new PointCh(MIN_E+20, MIN_N), new PointCh(MIN_E + 28, MIN_N),
                8, operand -> operand);

        le1.add(e1);
        le1.add(e2);
        lr.add(new SingleRoute(le1));

        le2.add(e3);
        le2.add(e4);

        lr.add(new SingleRoute(le2));

        List<PointCh> expected = new ArrayList<>();
        expected.add(new PointCh(MIN_E , MIN_N ));
        expected.add(new PointCh(MIN_E + 10, MIN_N));
        expected.add(new PointCh(MIN_E + 20, MIN_N));
        expected.add(new PointCh(MIN_E + 28, MIN_N));

        MultiRoute mr = new MultiRoute(lr);

        List<PointCh> actual = mr.points();

        assertEquals(expected, actual);
    }



    @Test
    public void indexOfSegmentAtTest(){
        List<Route> lr = new ArrayList<>();
        List<Edge> le1 = new ArrayList<>();
        List<Edge> le2 = new ArrayList<>();

        Edge e1 = new Edge(0, 1, new PointCh(MIN_E , MIN_N ), new PointCh(MIN_E + 10, MIN_N),
                10, operand -> operand);
        Edge e2 = new Edge(1, 2, new PointCh(MIN_E+10, MIN_N), new PointCh(MIN_E + 20, MIN_N),
                10, operand -> operand);

        Edge e3 = new Edge(1, 2, new PointCh(MIN_E+10, MIN_N), new PointCh(MIN_E + 20, MIN_N ),
                10, operand -> operand);
        Edge e4 = new Edge(3, 4, new PointCh(MIN_E+20, MIN_N), new PointCh(MIN_E + 28, MIN_N),
                8, operand -> operand);

        le1.add(e1);
        le1.add(e2);
        lr.add(new SingleRoute(le1));

        le2.add(e3);
        le2.add(e4);

        lr.add(new SingleRoute(le2));

        MultiRoute mr = new MultiRoute(lr);


        double position = 11;
        int expected = 0;
        int actual = mr.indexOfSegmentAt(position);
        assertEquals(expected, actual);
    }

    @Test
    public void indexOfSegmentAtTest2(){
        List<Route> lr = new ArrayList<>();
        List<Route> lr2 = new ArrayList<>();
        List<Edge> le1 = new ArrayList<>();
        List<Edge> le2 = new ArrayList<>();
        List<Edge> le3 = new ArrayList<>();

        Edge e1 = new Edge(0, 1, new PointCh(MIN_E , MIN_N ), new PointCh(MIN_E + 10, MIN_N),
                10, operand -> operand);
        Edge e2 = new Edge(1, 2, new PointCh(MIN_E+10, MIN_N), new PointCh(MIN_E + 20, MIN_N),
                10, operand -> operand);

        Edge e3 = new Edge(1, 2, new PointCh(MIN_E+10, MIN_N), new PointCh(MIN_E + 20, MIN_N ),
                10, operand -> operand);
        Edge e4 = new Edge(3, 4, new PointCh(MIN_E+20, MIN_N), new PointCh(MIN_E + 28, MIN_N),
                8, operand -> operand);

        Edge e5 = new Edge(0, 1, new PointCh(MIN_E , MIN_N ), new PointCh(MIN_E + 10, MIN_N),
                10, operand -> operand);
        Edge e6 = new Edge(1, 2, new PointCh(MIN_E+10, MIN_N), new PointCh(MIN_E + 20, MIN_N),
                10, operand -> operand);


        le1.add(e1);
        le1.add(e2);
        lr.add(new SingleRoute(le1));

        le2.add(e3);
        le2.add(e4);

        lr.add(new SingleRoute(le2));

        MultiRoute mr = new MultiRoute(lr);

        le3.add(e5);
        le3.add(e6);

        lr2.add(mr);
        lr2.add(new SingleRoute(le3));

        MultiRoute mr2 = new MultiRoute(lr2);



        double position = 47;
        int expected = 2;
        int actual = mr2.indexOfSegmentAt(position);
        assertEquals(expected, actual);
    }

    @Test
    public void indexOfSegmentAtTest3(){
        List<Route> lr = new ArrayList<>();
        List<Route> lr2 = new ArrayList<>();
        List<Route> lr3 = new ArrayList<>();

        List<Edge> le1 = new ArrayList<>();
        List<Edge> le2 = new ArrayList<>();
        List<Edge> le3 = new ArrayList<>();
        List<Edge> le4 = new ArrayList<>();
        List<Edge> le5 = new ArrayList<>();
        List<Edge> le6 = new ArrayList<>();


        Edge e1 = new Edge(0, 1, new PointCh(MIN_E , MIN_N ), new PointCh(MIN_E + 10, MIN_N),
                1000, operand -> operand);
        Edge e2 = new Edge(1, 2, new PointCh(MIN_E+10, MIN_N), new PointCh(MIN_E + 20, MIN_N),
                1000, operand -> operand);

        Edge e3 = new Edge(1, 2, new PointCh(MIN_E+10, MIN_N), new PointCh(MIN_E + 20, MIN_N ),
                1000, operand -> operand);
        Edge e4 = new Edge(3, 4, new PointCh(MIN_E+20, MIN_N), new PointCh(MIN_E + 28, MIN_N),
                1000, operand -> operand);

        Edge e5 = new Edge(0, 1, new PointCh(MIN_E , MIN_N ), new PointCh(MIN_E + 10, MIN_N),
                1000, operand -> operand);
        Edge e6 = new Edge(1, 2, new PointCh(MIN_E+10, MIN_N), new PointCh(MIN_E + 20, MIN_N),
                1000, operand -> operand);



        le1.add(e1);
        lr.add(new SingleRoute(le1));

        le2.add(e2);
        lr.add(new SingleRoute(le2));

        le3.add(e3);
        lr.add(new SingleRoute(le3));

        MultiRoute mr = new MultiRoute(lr);

        le4.add(e4);
        lr2.add(new SingleRoute(le4));

        le5.add(e5);
        lr2.add(new SingleRoute(le5));

        le6.add(e6);
        lr2.add(new SingleRoute(le6));

        MultiRoute mr2 = new MultiRoute(lr2);

        lr3.add(mr);
        lr3.add(mr2);

        MultiRoute mr3 = new MultiRoute(lr3);
        double position = 5500;
        int expected = 5;

        int actual = mr3.indexOfSegmentAt(position);

        assertEquals(expected, actual);

    }

    @Test
    public void indexOfSegmentAtTest4(){
        List<Route> lr = new ArrayList<>();
        List<Route> lr2 = new ArrayList<>();
        List<Route> lr3 = new ArrayList<>();


        List<Edge> le1 = new ArrayList<>();

        Edge e1 = new Edge(0, 1, new PointCh(MIN_E , MIN_N ), new PointCh(MIN_E + 10, MIN_N),
                1000, operand -> operand);



        le1.add(e1);
        lr.add(new SingleRoute(le1));

        MultiRoute mr = new MultiRoute(lr);

        MultiRoute mr3 = new MultiRoute(lr);

        MultiRoute mr4 = new MultiRoute(lr);

        lr3.add(mr3);
        lr3.add(mr4);

        MultiRoute mr2 = new MultiRoute(lr3);

        lr2.add(mr);
        lr2.add(mr2);
        MultiRoute mr5 = new MultiRoute(lr2);


        double position = 1999;

        int expected = 1;

        int actual = mr5.indexOfSegmentAt(position);

        assertEquals(expected, actual);
    }

    @Test

    public void PointAtTest(){
        List<Route> lr = new ArrayList<>();
        List<Route> lr2 = new ArrayList<>();
        List<Route> lr3 = new ArrayList<>();


        List<Edge> le1 = new ArrayList<>();

        Edge e1 = new Edge(0, 1, new PointCh(MIN_E , MIN_N ), new PointCh(MIN_E + 10, MIN_N),
                1000, operand -> operand);



        le1.add(e1);
        lr.add(new SingleRoute(le1));

        MultiRoute mr = new MultiRoute(lr);

        MultiRoute mr3 = new MultiRoute(lr);

        MultiRoute mr4 = new MultiRoute(lr);

        lr3.add(mr3);
        lr3.add(mr4);

        MultiRoute mr2 = new MultiRoute(lr3);

        lr2.add(mr);
        lr2.add(mr2);
        MultiRoute mr5 = new MultiRoute(lr2);


        double position = 0;

        PointCh expected = new PointCh(MIN_E , MIN_N );

        PointCh actual = mr5.pointAt(position);

        assertEquals(expected, actual);
    }

    @Test
    public void PointAtTest2(){
        var rng = newRandom();

        List<Route> lr = new ArrayList<>();
        List<Route> lr2 = new ArrayList<>();
        List<Route> lr3 = new ArrayList<>();

        List<Edge> le1 = new ArrayList<>();
        List<Edge> le2 = new ArrayList<>();
        List<Edge> le3 = new ArrayList<>();
        List<Edge> le4 = new ArrayList<>();
        List<Edge> le5 = new ArrayList<>();
        List<Edge> le6 = new ArrayList<>();


        Edge e1 = new Edge(0, 1, new PointCh(MIN_E , MIN_N ), new PointCh(MIN_E + 10, MIN_N),
                10, operand -> operand);
        Edge e2 = new Edge(1, 2, new PointCh(MIN_E + 10 , MIN_N ), new PointCh(MIN_E + 20, MIN_N),
                10, operand -> operand);

        le1.add(e1);
        le1.add(e2);

        Edge e3 = new Edge(2, 3, new PointCh(MIN_E + 20 , MIN_N ), new PointCh(MIN_E + 30, MIN_N),
                10, operand -> operand);
        Edge e4 = new Edge(3, 4, new PointCh(MIN_E + 30 , MIN_N ), new PointCh(MIN_E + 40, MIN_N),
                10, operand -> operand);

        le2.add(e3);
        le2.add(e4);

        Edge e5 = new Edge(2, 3, new PointCh(MIN_E + 20 , MIN_N ), new PointCh(MIN_E + 30, MIN_N),
                10, operand -> operand);
        Edge e6 = new Edge(3, 4, new PointCh(MIN_E + 30 , MIN_N ), new PointCh(MIN_E + 40, MIN_N),
                10, operand -> operand);

        le3.add(e5);
        le3.add(e6);

        Edge e7 = new Edge(3, 4, new PointCh(MIN_E + 40 , MIN_N ), new PointCh(MIN_E + 50, MIN_N),
                10, operand -> operand);
        Edge e8 = new Edge(4, 5, new PointCh(MIN_E + 50 , MIN_N ), new PointCh(MIN_E + 60, MIN_N),
                10, operand -> operand);

        le4.add(e7);
        le4.add(e8);

        Edge e9 = new Edge(4, 5, new PointCh(MIN_E + 60 , MIN_N ), new PointCh(MIN_E + 70, MIN_N),
                10, operand -> operand);
        Edge e10 = new Edge(5, 6, new PointCh(MIN_E + 70 , MIN_N ), new PointCh(MIN_E + 80, MIN_N),
                10, operand -> operand);

        le5.add(e9);
        le5.add(e10);

        Edge e11 = new Edge(6, 7, new PointCh(MIN_E + 80 , MIN_N ), new PointCh(MIN_E + 90, MIN_N),
                10, operand -> operand);
        Edge e12 = new Edge(7, 8, new PointCh(MIN_E + 90 , MIN_N ), new PointCh(MIN_E + 1000, MIN_N),
                10, operand -> operand);

        le6.add(e11);
        le6.add(e12);


        lr.add(new SingleRoute(le1));
        lr.add(new SingleRoute(le2));
        lr.add(new SingleRoute(le3));
        lr2.add(new SingleRoute(le4));
        lr2.add(new SingleRoute(le5));
        lr2.add(new SingleRoute(le6));

        MultiRoute mr = new MultiRoute(lr);

        MultiRoute mr2 = new MultiRoute(lr2);

        lr3.add(mr);
        lr3.add(mr2);
        MultiRoute mr3 = new MultiRoute(lr3);

        double position = rng.nextDouble(0, mr3.length()) ;

        PointCh expected = new PointCh(MIN_E + position , MIN_N );

        PointCh actual = mr3.pointAt(position);

        assertEquals(expected, actual);
    }

    @Test
    public void PointAtTest2_10Times(){
        for(int i=0; i < 10; i++){
            PointAtTest2();
        }
    }


    @Test
    void singleRoutePointClosestToWorksWithFarAwayPoints() {
        var rng = newRandom();
        var r_route = new SingleRoute(verticalEdges(1));
        var route = new MultiRoute(new ArrayList<Route>(Collections.singleton(r_route)));

        // Points below the route
        var origin = new PointCh(ORIGIN_E, ORIGIN_N);
        for (int i = 0; i < RANDOM_ITERATIONS; i += 1) {
            var dN = rng.nextDouble(-10_000, -1);
            var dE = rng.nextDouble(-1000, 1000);
            var p = new PointCh(ORIGIN_E + dE, ORIGIN_N + dN);
            var pct = route.pointClosestTo(p);
            assertEquals(origin, pct.point());
            assertEquals(0, pct.position());
            assertEquals(Math.hypot(dE, dN), pct.distanceToReference(), 1e-4);
        }

        // Points above the route
        var end = new PointCh(ORIGIN_E, ORIGIN_N + EDGE_LENGTH);
        for (int i = 0; i < RANDOM_ITERATIONS; i += 1) {
            var dN = rng.nextDouble(1, 10_000);
            var dE = rng.nextDouble(-1000, 1000);
            var p = new PointCh(ORIGIN_E + dE, ORIGIN_N + EDGE_LENGTH + dN);
            var pct = route.pointClosestTo(p);
            //assertEquals(end, pct.point());
            assertEquals(EDGE_LENGTH, pct.position());
            assertEquals(Math.hypot(dE, dN), pct.distanceToReference(), 1e-4);
        }
    }

    @Test
    void singleRoutePointClosestToWorksWithPointsOnRoute() {
        var rng = newRandom();
        var r_route = new SingleRoute(verticalEdges(20));
        var route = new MultiRoute(new ArrayList<Route>(Collections.singleton(r_route)));

        for (int i = 0; i < RANDOM_ITERATIONS; i += 1) {
            var pos = rng.nextDouble(0, route.length());
            var pt = route.pointAt(pos);
            var pct = route.pointClosestTo(pt);
            assertEquals(pt.e(), pct.point().e(), 1e-4);
            assertEquals(pt.n(), pct.point().n(), 1e-4);
            assertEquals(pos, pct.position(), 1e-4);
            assertEquals(0, pct.distanceToReference(), 1e-4);
        }
    }

    @Test
    void singleRoutePointClosestToWorksWithSawtoothPoints() {
        var edgesCount = 4;
        var edges = sawToothEdges(edgesCount);
        var r_route = new SingleRoute(edges);
        var route = new MultiRoute(new ArrayList<Route>(Collections.singleton(r_route)));


        // Points above the sawtooth
        for (int i = 1; i <= edgesCount; i += 2) {
            var p = sawToothPoint(i);
            var dN = i * 500;
            var pAbove = new PointCh(p.e(), p.n() + dN);
            var pct = route.pointClosestTo(pAbove);
            assertEquals(p, pct.point());
            assertEquals(i * TOOTH_LENGTH, pct.position());
            assertEquals(dN, pct.distanceToReference());
        }

        // Points below the sawtooth
        for (int i = 0; i <= edgesCount; i += 2) {
            var p = sawToothPoint(i);
            var dN = i * 500;
            var pBelow = new PointCh(p.e(), p.n() - dN);
            var pct = route.pointClosestTo(pBelow);
            assertEquals(p, pct.point());
            assertEquals(i * TOOTH_LENGTH, pct.position());
            assertEquals(dN, pct.distanceToReference());
        }

        // Points close to the n/8
        var dE = TOOTH_NS / 16d;
        var dN = TOOTH_EW / 16d;
        for (int i = 0; i < edgesCount; i += 1) {
            var upwardEdge = (i & 1) == 0;
            for (double p = 0.125; p <= 0.875; p += 0.125) {
                var pointE = ORIGIN_E + (i + p) * TOOTH_EW;
                var pointN = ORIGIN_N + TOOTH_NS * (upwardEdge ? p : (1 - p));
                var point = new PointCh(pointE, pointN);
                var position = (i + p) * TOOTH_LENGTH;
                var reference = new PointCh(
                        pointE + dE,
                        pointN + (upwardEdge ? -dN : dN));
                var pct = route.pointClosestTo(reference);
                assertEquals(point, pct.point());
                assertEquals(position, pct.position());
                assertEquals(Math.hypot(dE, dN), pct.distanceToReference());
            }
        }
    }

    @Test
    public void pointClosestToTest1(){
        var rng = newRandom();

        List<Route> lr = new ArrayList<>();
        List<Route> lr2 = new ArrayList<>();
        List<Route> lr3 = new ArrayList<>();

        List<Edge> le1 = new ArrayList<>();
        List<Edge> le2 = new ArrayList<>();
        List<Edge> le3 = new ArrayList<>();
        List<Edge> le4 = new ArrayList<>();
        List<Edge> le5 = new ArrayList<>();
        List<Edge> le6 = new ArrayList<>();


        Edge e1 = new Edge(0, 1, new PointCh(MIN_E , MIN_N ), new PointCh(MIN_E + 10, MIN_N),
                10, operand -> operand);
        Edge e2 = new Edge(1, 2, new PointCh(MIN_E + 10 , MIN_N ), new PointCh(MIN_E + 20, MIN_N),
                10, operand -> operand);

        le1.add(e1);
        le1.add(e2);

        Edge e3 = new Edge(2, 3, new PointCh(MIN_E + 20 , MIN_N ), new PointCh(MIN_E + 30, MIN_N),
                10, operand -> operand);
        Edge e4 = new Edge(3, 4, new PointCh(MIN_E + 30 , MIN_N ), new PointCh(MIN_E + 40, MIN_N),
                10, operand -> operand);

        le2.add(e3);
        le2.add(e4);

        Edge e5 = new Edge(2, 3, new PointCh(MIN_E + 40 , MIN_N ), new PointCh(MIN_E + 50, MIN_N),
                10, operand -> operand);
        Edge e6 = new Edge(3, 4, new PointCh(MIN_E + 50 , MIN_N ), new PointCh(MIN_E + 60, MIN_N),
                10, operand -> operand);

        le3.add(e5);
        le3.add(e6);

        Edge e7 = new Edge(3, 4, new PointCh(MIN_E + 60 , MIN_N ), new PointCh(MIN_E + 70, MIN_N),
                10, operand -> operand);
        Edge e8 = new Edge(4, 5, new PointCh(MIN_E + 70 , MIN_N ), new PointCh(MIN_E + 80, MIN_N),
                10, operand -> operand);

        le4.add(e7);
        le4.add(e8);

        Edge e9 = new Edge(4, 5, new PointCh(MIN_E + 80 , MIN_N ), new PointCh(MIN_E + 90, MIN_N),
                10, operand -> operand);
        Edge e10 = new Edge(5, 6, new PointCh(MIN_E + 90 , MIN_N ), new PointCh(MIN_E + 100, MIN_N),
                10, operand -> operand);

        le5.add(e9);
        le5.add(e10);

        Edge e11 = new Edge(6, 7, new PointCh(MIN_E + 100 , MIN_N ), new PointCh(MIN_E + 110, MIN_N),
                10, operand -> operand);
        Edge e12 = new Edge(7, 8, new PointCh(MIN_E + 110 , MIN_N ), new PointCh(MIN_E + 120, MIN_N),
                10, operand -> operand);

        le6.add(e11);
        le6.add(e12);


        lr.add(new SingleRoute(le1));
        lr.add(new SingleRoute(le2));
        lr.add(new SingleRoute(le3));
        lr2.add(new SingleRoute(le4));
        lr2.add(new SingleRoute(le5));
        lr2.add(new SingleRoute(le6));

        MultiRoute mr = new MultiRoute(lr);

        MultiRoute mr2 = new MultiRoute(lr2);

        lr3.add(mr);
        lr3.add(mr2);
        MultiRoute mr3 = new MultiRoute(lr3);
        double position =rng.nextDouble(0,120);
        PointCh point = new PointCh(MIN_E + position, MIN_N );

        RoutePoint expected = new RoutePoint(point, position, 0);

        RoutePoint actual = mr3.pointClosestTo(point);

        assertEquals(expected, actual);

    }


    @Test
    public void lengthTest2(){

        List<Route> lr = new ArrayList<>();
        List<Route> lr2 = new ArrayList<>();
        List<Route> lr3 = new ArrayList<>();

        List<Edge> le1 = new ArrayList<>();
        List<Edge> le2 = new ArrayList<>();
        List<Edge> le3 = new ArrayList<>();
        List<Edge> le4 = new ArrayList<>();
        List<Edge> le5 = new ArrayList<>();
        List<Edge> le6 = new ArrayList<>();


        Edge e1 = new Edge(0, 1, new PointCh(MIN_E , MIN_N ), new PointCh(MIN_E + 10, MIN_N),
                10, operand -> operand);
        Edge e2 = new Edge(1, 2, new PointCh(MIN_E + 10 , MIN_N ), new PointCh(MIN_E + 20, MIN_N),
                10, operand -> operand);

        le1.add(e1);
        le1.add(e2);

        Edge e3 = new Edge(2, 3, new PointCh(MIN_E + 20 , MIN_N ), new PointCh(MIN_E + 30, MIN_N),
                10, operand -> operand);
        Edge e4 = new Edge(3, 4, new PointCh(MIN_E + 30 , MIN_N ), new PointCh(MIN_E + 40, MIN_N),
                10, operand -> operand);

        le2.add(e3);
        le2.add(e4);

        Edge e5 = new Edge(2, 3, new PointCh(MIN_E + 40 , MIN_N ), new PointCh(MIN_E + 50, MIN_N),
                10, operand -> operand);
        Edge e6 = new Edge(3, 4, new PointCh(MIN_E + 50 , MIN_N ), new PointCh(MIN_E + 60, MIN_N),
                10, operand -> operand);

        le3.add(e5);
        le3.add(e6);

        Edge e7 = new Edge(3, 4, new PointCh(MIN_E + 60 , MIN_N ), new PointCh(MIN_E + 70, MIN_N),
                10, operand -> operand);
        Edge e8 = new Edge(4, 5, new PointCh(MIN_E + 70 , MIN_N ), new PointCh(MIN_E + 80, MIN_N),
                10, operand -> operand);

        le4.add(e7);
        le4.add(e8);

        Edge e9 = new Edge(4, 5, new PointCh(MIN_E + 80 , MIN_N ), new PointCh(MIN_E + 90, MIN_N),
                10, operand -> operand);
        Edge e10 = new Edge(5, 6, new PointCh(MIN_E + 90 , MIN_N ), new PointCh(MIN_E + 100, MIN_N),
                10, operand -> operand);

        le5.add(e9);
        le5.add(e10);

        Edge e11 = new Edge(6, 7, new PointCh(MIN_E + 100 , MIN_N ), new PointCh(MIN_E + 110, MIN_N),
                10, operand -> operand);
        Edge e12 = new Edge(7, 8, new PointCh(MIN_E + 110 , MIN_N ), new PointCh(MIN_E + 120, MIN_N),
                10, operand -> operand);

        le6.add(e11);
        le6.add(e12);


        lr.add(new SingleRoute(le1));
        lr.add(new SingleRoute(le2));
        lr.add(new SingleRoute(le3));
        lr2.add(new SingleRoute(le4));
        lr2.add(new SingleRoute(le5));
        lr2.add(new SingleRoute(le6));

        MultiRoute mr = new MultiRoute(lr);

        MultiRoute mr2 = new MultiRoute(lr2);

        lr3.add(mr);
        lr3.add(mr2);
        MultiRoute mr3 = new MultiRoute(lr3);

        double expected = 120;

        double actual = mr3.length();

        assertEquals(expected, actual);

    }

    @Test
    public void edgesTest2(){

        List<Route> lr = new ArrayList<>();
        List<Route> lr2 = new ArrayList<>();
        List<Route> lr3 = new ArrayList<>();

        List<Edge> le1 = new ArrayList<>();
        List<Edge> le2 = new ArrayList<>();
        List<Edge> le3 = new ArrayList<>();
        List<Edge> le4 = new ArrayList<>();
        List<Edge> le5 = new ArrayList<>();
        List<Edge> le6 = new ArrayList<>();


        Edge e1 = new Edge(0, 1, new PointCh(MIN_E , MIN_N ), new PointCh(MIN_E + 10, MIN_N),
                10, operand -> operand);
        Edge e2 = new Edge(1, 2, new PointCh(MIN_E + 10 , MIN_N ), new PointCh(MIN_E + 20, MIN_N),
                10, operand -> operand);

        le1.add(e1);
        le1.add(e2);

        Edge e3 = new Edge(2, 3, new PointCh(MIN_E + 20 , MIN_N ), new PointCh(MIN_E + 30, MIN_N),
                10, operand -> operand);
        Edge e4 = new Edge(3, 4, new PointCh(MIN_E + 30 , MIN_N ), new PointCh(MIN_E + 40, MIN_N),
                10, operand -> operand);

        le2.add(e3);
        le2.add(e4);

        Edge e5 = new Edge(2, 3, new PointCh(MIN_E + 40 , MIN_N ), new PointCh(MIN_E + 50, MIN_N),
                10, operand -> operand);
        Edge e6 = new Edge(3, 4, new PointCh(MIN_E + 50 , MIN_N ), new PointCh(MIN_E + 60, MIN_N),
                10, operand -> operand);

        le3.add(e5);
        le3.add(e6);

        Edge e7 = new Edge(3, 4, new PointCh(MIN_E + 60 , MIN_N ), new PointCh(MIN_E + 70, MIN_N),
                10, operand -> operand);
        Edge e8 = new Edge(4, 5, new PointCh(MIN_E + 70 , MIN_N ), new PointCh(MIN_E + 80, MIN_N),
                10, operand -> operand);

        le4.add(e7);
        le4.add(e8);

        Edge e9 = new Edge(4, 5, new PointCh(MIN_E + 80 , MIN_N ), new PointCh(MIN_E + 90, MIN_N),
                10, operand -> operand);
        Edge e10 = new Edge(5, 6, new PointCh(MIN_E + 90 , MIN_N ), new PointCh(MIN_E + 100, MIN_N),
                10, operand -> operand);

        le5.add(e9);
        le5.add(e10);

        Edge e11 = new Edge(6, 7, new PointCh(MIN_E + 100 , MIN_N ), new PointCh(MIN_E + 110, MIN_N),
                10, operand -> operand);
        Edge e12 = new Edge(7, 8, new PointCh(MIN_E + 110 , MIN_N ), new PointCh(MIN_E + 120, MIN_N),
                10, operand -> operand);

        le6.add(e11);
        le6.add(e12);


        lr.add(new SingleRoute(le1));
        lr.add(new SingleRoute(le2));
        lr.add(new SingleRoute(le3));
        lr2.add(new SingleRoute(le4));
        lr2.add(new SingleRoute(le5));
        lr2.add(new SingleRoute(le6));

        MultiRoute mr = new MultiRoute(lr);

        MultiRoute mr2 = new MultiRoute(lr2);

        lr3.add(mr);
        lr3.add(mr2);
        MultiRoute mr3 = new MultiRoute(lr3);

        List<Edge> expected = new ArrayList<>();
        expected.add(e1);
        expected.add(e2);
        expected.add(e3);
        expected.add(e4);
        expected.add(e5);
        expected.add(e6);
        expected.add(e7);
        expected.add(e8);
        expected.add(e9);
        expected.add(e10);
        expected.add(e11);
        expected.add(e12);

        List<Edge> actual = mr3.edges();

        assertEquals(expected, actual);

    }
    
    @Test
    public void nodeClosestTo(){
        double position = Math.floor(Math.random()*(40+1));

        List<Route> lr = new ArrayList<>();
        List<Route> lr2 = new ArrayList<>();
        List<Route> lr3 = new ArrayList<>();

        List<Edge> le1 = new ArrayList<>();
        List<Edge> le2 = new ArrayList<>();
        List<Edge> le3 = new ArrayList<>();
        List<Edge> le4 = new ArrayList<>();
        List<Edge> le5 = new ArrayList<>();
        List<Edge> le6 = new ArrayList<>();


        Edge e1 = new Edge(0, 1, new PointCh(MIN_E , MIN_N ), new PointCh(MIN_E + 10, MIN_N),
                10, operand -> operand);
        Edge e2 = new Edge(1, 2, new PointCh(MIN_E + 10 , MIN_N ), new PointCh(MIN_E + 20, MIN_N),
                10, operand -> operand+1);

        le1.add(e1);
        le1.add(e2);

        Edge e3 = new Edge(2, 3, new PointCh(MIN_E + 20 , MIN_N ), new PointCh(MIN_E + 30, MIN_N),
                10, operand -> operand+2);
        Edge e4 = new Edge(3, 4, new PointCh(MIN_E + 30 , MIN_N ), new PointCh(MIN_E + 40, MIN_N),
                10, operand -> operand+3);

        le2.add(e3);
        le2.add(e4);

        Edge e5 = new Edge(4, 5, new PointCh(MIN_E + 40 , MIN_N ), new PointCh(MIN_E + 50, MIN_N),
                10, operand -> operand+4);
        Edge e6 = new Edge(5, 6, new PointCh(MIN_E + 50 , MIN_N ), new PointCh(MIN_E + 60, MIN_N),
                10, operand -> operand+5);

        le3.add(e5);
        le3.add(e6);

        Edge e7 = new Edge(6, 7, new PointCh(MIN_E + 60 , MIN_N ), new PointCh(MIN_E + 70, MIN_N),
                10, operand -> operand+6);
        Edge e8 = new Edge(7, 8, new PointCh(MIN_E + 70 , MIN_N ), new PointCh(MIN_E + 80, MIN_N),
                10, operand -> operand+7);

        le4.add(e7);
        le4.add(e8);

        Edge e9 = new Edge(8, 9, new PointCh(MIN_E + 80 , MIN_N ), new PointCh(MIN_E + 90, MIN_N),
                10, operand -> operand+8);
        Edge e10 = new Edge(9, 10, new PointCh(MIN_E + 90 , MIN_N ), new PointCh(MIN_E + 100, MIN_N),
                10, operand -> operand+9);

        le5.add(e9);
        le5.add(e10);

        Edge e11 = new Edge(10, 11, new PointCh(MIN_E + 100 , MIN_N ), new PointCh(MIN_E + 110, MIN_N),
                10, operand -> operand+10);
        Edge e12 = new Edge(11, 12, new PointCh(MIN_E + 110 , MIN_N ), new PointCh(MIN_E + 120, MIN_N),
                10, operand -> operand+11);

        le6.add(e11);
        le6.add(e12);


        lr.add(new SingleRoute(le1));
        lr.add(new SingleRoute(le2));
        lr.add(new SingleRoute(le3));
        lr2.add(new SingleRoute(le4));
        lr2.add(new SingleRoute(le5));
        lr2.add(new SingleRoute(le6));

        MultiRoute mr = new MultiRoute(lr);

        MultiRoute mr2 = new MultiRoute(lr2);

        lr3.add(mr);
        lr3.add(mr2);
        MultiRoute mr3 = new MultiRoute(lr3);

        int expected = 0;

        if(position >= 5 && position <= 15){
             expected = 1;
        }
        if(position >= 15 && position <= 25){
             expected = 2;
        }
        if(position >= 25 && position < 35){
             expected = 3;
        }
        if(position >= 35 && position <= 40){
             expected = 4;
        }
        if(position >= 45 && position <= 55){
             expected = 5;
        }


        int actual = mr3.nodeClosestTo(position);

        assertEquals(expected, actual);
    }

    @Test

    public void elevationAtTest(){
        var rng = newRandom();

        List<Route> lr = new ArrayList<>();
        List<Route> lr2 = new ArrayList<>();
        List<Route> lr3 = new ArrayList<>();

        List<Edge> le1 = new ArrayList<>();
        List<Edge> le2 = new ArrayList<>();
        List<Edge> le3 = new ArrayList<>();
        List<Edge> le4 = new ArrayList<>();
        List<Edge> le5 = new ArrayList<>();
        List<Edge> le6 = new ArrayList<>();


        Edge e1 = new Edge(0, 1, new PointCh(MIN_E , MIN_N ), new PointCh(MIN_E + 10, MIN_N),
                10, operand -> operand);
        Edge e2 = new Edge(1, 2, new PointCh(MIN_E + 10 , MIN_N ), new PointCh(MIN_E + 20, MIN_N),
                10, operand -> operand+1);

        le1.add(e1);
        le1.add(e2);

        Edge e3 = new Edge(2, 3, new PointCh(MIN_E + 20 , MIN_N ), new PointCh(MIN_E + 30, MIN_N),
                10, operand -> operand+2);
        Edge e4 = new Edge(3, 4, new PointCh(MIN_E + 30 , MIN_N ), new PointCh(MIN_E + 40, MIN_N),
                10, operand -> operand+3);

        le2.add(e3);
        le2.add(e4);

        Edge e5 = new Edge(2, 3, new PointCh(MIN_E + 40 , MIN_N ), new PointCh(MIN_E + 50, MIN_N),
                10, operand -> operand+4);
        Edge e6 = new Edge(3, 4, new PointCh(MIN_E + 50 , MIN_N ), new PointCh(MIN_E + 60, MIN_N),
                10, operand -> operand+5);

        le3.add(e5);
        le3.add(e6);

        Edge e7 = new Edge(3, 4, new PointCh(MIN_E + 60 , MIN_N ), new PointCh(MIN_E + 70, MIN_N),
                10, operand -> operand+6);
        Edge e8 = new Edge(4, 5, new PointCh(MIN_E + 70 , MIN_N ), new PointCh(MIN_E + 80, MIN_N),
                10, operand -> operand+7);

        le4.add(e7);
        le4.add(e8);

        Edge e9 = new Edge(4, 5, new PointCh(MIN_E + 80 , MIN_N ), new PointCh(MIN_E + 90, MIN_N),
                10, operand -> operand+8);
        Edge e10 = new Edge(5, 6, new PointCh(MIN_E + 90 , MIN_N ), new PointCh(MIN_E + 100, MIN_N),
                10, operand -> operand+9);

        le5.add(e9);
        le5.add(e10);

        Edge e11 = new Edge(6, 7, new PointCh(MIN_E + 100 , MIN_N ), new PointCh(MIN_E + 110, MIN_N),
                10, operand -> operand+10);
        Edge e12 = new Edge(7, 8, new PointCh(MIN_E + 110 , MIN_N ), new PointCh(MIN_E + 120, MIN_N),
                10, operand -> operand+11);

        le6.add(e11);
        le6.add(e12);


        lr.add(new SingleRoute(le1));
        lr.add(new SingleRoute(le2));
        lr.add(new SingleRoute(le3));
        lr2.add(new SingleRoute(le4));
        lr2.add(new SingleRoute(le5));
        lr2.add(new SingleRoute(le6));

        MultiRoute mr = new MultiRoute(lr);

        MultiRoute mr2 = new MultiRoute(lr2);

        lr3.add(mr);
        lr3.add(mr2);
        MultiRoute mr3 = new MultiRoute(lr3);

        double position = rng.nextDouble(0, 40);

        double expected = position;

        if(position > 10 && position <= 20){
            expected = position+1 - 10;
        }
        if(position > 20 && position <= 30){
            expected = position+2 - 20;
        }
        if(position > 30 && position < 40){
            expected = position+3 - 30;
        }
        if(position >= 40 && position <= 50){
            expected = position+4 - 40;
        }
        if(position > 50 && position <= 60){
            expected = position+5-50;
        }
        if(position > 60 && position <= 70){
            expected = position+6 - 60;
        }
        if(position > 70 && position <= 80){
            expected = position+7 - 70;
        }
        if(position > 80 && position <= 90){
            expected = position+8 - 80;
        }
        if(position > 90 && position <= 100){
            expected = position+9 - 90;
        }
        if(position > 100 && position <= 110){
            expected = position+10 - 100;
        }
        if(position > 110 && position <= 120){
            expected = position+11 - 110;
        }


        double actual = mr3.elevationAt(position);

        assertEquals(expected, actual);
    }

    @Test

    public void pointsTest1(){
        var rng = newRandom();

        List<Route> lr = new ArrayList<>();
        List<Route> lr2 = new ArrayList<>();
        List<Route> lr3 = new ArrayList<>();

        List<Edge> le1 = new ArrayList<>();
        List<Edge> le2 = new ArrayList<>();
        List<Edge> le3 = new ArrayList<>();
        List<Edge> le4 = new ArrayList<>();
        List<Edge> le5 = new ArrayList<>();
        List<Edge> le6 = new ArrayList<>();


        Edge e1 = new Edge(0, 1, new PointCh(MIN_E , MIN_N ), new PointCh(MIN_E + 10, MIN_N),
                10, operand -> operand);
        Edge e2 = new Edge(1, 2, new PointCh(MIN_E + 10 , MIN_N ), new PointCh(MIN_E + 20, MIN_N),
                10, operand -> operand+1);

        le1.add(e1);
        le1.add(e2);

        Edge e3 = new Edge(2, 3, new PointCh(MIN_E + 20 , MIN_N ), new PointCh(MIN_E + 30, MIN_N),
                10, operand -> operand+2);
        Edge e4 = new Edge(3, 4, new PointCh(MIN_E + 30 , MIN_N ), new PointCh(MIN_E + 40, MIN_N),
                10, operand -> operand+3);

        le2.add(e3);
        le2.add(e4);

        Edge e5 = new Edge(2, 3, new PointCh(MIN_E + 40 , MIN_N ), new PointCh(MIN_E + 50, MIN_N),
                10, operand -> operand+4);
        Edge e6 = new Edge(3, 4, new PointCh(MIN_E + 50 , MIN_N ), new PointCh(MIN_E + 60, MIN_N),
                10, operand -> operand+5);

        le3.add(e5);
        le3.add(e6);

        Edge e7 = new Edge(3, 4, new PointCh(MIN_E + 60 , MIN_N ), new PointCh(MIN_E + 70, MIN_N),
                10, operand -> operand+6);
        Edge e8 = new Edge(4, 5, new PointCh(MIN_E + 70 , MIN_N ), new PointCh(MIN_E + 80, MIN_N),
                10, operand -> operand+7);

        le4.add(e7);
        le4.add(e8);

        Edge e9 = new Edge(4, 5, new PointCh(MIN_E + 80 , MIN_N ), new PointCh(MIN_E + 90, MIN_N),
                10, operand -> operand+8);
        Edge e10 = new Edge(5, 6, new PointCh(MIN_E + 90 , MIN_N ), new PointCh(MIN_E + 100, MIN_N),
                10, operand -> operand+9);

        le5.add(e9);
        le5.add(e10);

        Edge e11 = new Edge(6, 7, new PointCh(MIN_E + 100 , MIN_N ), new PointCh(MIN_E + 110, MIN_N),
                10, operand -> operand+10);
        Edge e12 = new Edge(7, 8, new PointCh(MIN_E + 110 , MIN_N ), new PointCh(MIN_E + 120, MIN_N),
                10, operand -> operand+11);

        le6.add(e11);
        le6.add(e12);


        lr.add(new SingleRoute(le1));
        lr.add(new SingleRoute(le2));
        lr.add(new SingleRoute(le3));
        lr2.add(new SingleRoute(le4));
        lr2.add(new SingleRoute(le5));
        lr2.add(new SingleRoute(le6));

        MultiRoute mr = new MultiRoute(lr);

        MultiRoute mr2 = new MultiRoute(lr2);

        lr3.add(mr);
        lr3.add(mr2);
        MultiRoute mr3 = new MultiRoute(lr3);

        double position = rng.nextDouble(0, 40);

        List<PointCh> expected = new ArrayList<>();
        expected.add(new PointCh(MIN_E , MIN_N ));
        expected.add(new PointCh(MIN_E + 10 , MIN_N ));
        expected.add(new PointCh(MIN_E + 20 , MIN_N ));
        expected.add(new PointCh(MIN_E + 30 , MIN_N ));
        expected.add(new PointCh(MIN_E + 40 , MIN_N ));
        expected.add(new PointCh(MIN_E + 50 , MIN_N ));
        expected.add(new PointCh(MIN_E + 60 , MIN_N ));
        expected.add(new PointCh(MIN_E + 70 , MIN_N ));
        expected.add(new PointCh(MIN_E + 80 , MIN_N ));
        expected.add(new PointCh(MIN_E + 90 , MIN_N ));
        expected.add(new PointCh(MIN_E + 100 , MIN_N ));
        expected.add( new PointCh(MIN_E + 110 , MIN_N ));
        expected.add(new PointCh(MIN_E + 120, MIN_N));

        List<PointCh> actual = mr3.points();

        assertEquals(expected, actual);
    }

    @Test

    public void pointTest2(){

        List<Route> lr = new ArrayList<>();
        List<Route> lr3 = new ArrayList<>();

        List<Edge> le1 = new ArrayList<>();
        List<Edge> le2 = new ArrayList<>();
        List<Edge> le3 = new ArrayList<>();



        Edge e1 = new Edge(0, 1, new PointCh(MIN_E , MIN_N ), new PointCh(MIN_E + 10, MIN_N),
                10, operand -> operand);
        Edge e2 = new Edge(1, 2, new PointCh(MIN_E + 10 , MIN_N ), new PointCh(MIN_E + 20, MIN_N),
                10, operand -> operand+1);

        le1.add(e1);
        le1.add(e2);

        Edge e3 = new Edge(2, 3, new PointCh(MIN_E + 20 , MIN_N ), new PointCh(MIN_E + 30, MIN_N),
                10, operand -> operand+2);
        Edge e4 = new Edge(3, 4, new PointCh(MIN_E + 30 , MIN_N ), new PointCh(MIN_E + 40, MIN_N),
                10, operand -> operand+3);

        le2.add(e3);
        le2.add(e4);

        Edge e5 = new Edge(4, 5, new PointCh(MIN_E + 40 , MIN_N ), new PointCh(MIN_E + 50, MIN_N),
                10, operand -> operand+4);
        Edge e6 = new Edge(5, 0, new PointCh(MIN_E + 50 , MIN_N ), new PointCh(MIN_E, MIN_N),
                10, operand -> operand+5);
        le3.add(e5);
        le3.add(e6);

        lr.add(new SingleRoute(le1));
        lr.add(new SingleRoute(le2));
        lr.add(new SingleRoute(le3));

        MultiRoute mr = new MultiRoute(lr);

        lr3.add(mr);
        MultiRoute mr3 = new MultiRoute(lr3);


        List<PointCh> expected = new ArrayList<>();
        expected.add(new PointCh(MIN_E , MIN_N ));
        expected.add(new PointCh(MIN_E + 10 , MIN_N ));
        expected.add(new PointCh(MIN_E + 20 , MIN_N ));
        expected.add(new PointCh(MIN_E + 30 , MIN_N ));
        expected.add(new PointCh(MIN_E + 40 , MIN_N ));
        expected.add(new PointCh(MIN_E + 50 , MIN_N ));
        expected.add(new PointCh(MIN_E, MIN_N ));

        List<PointCh> actual = mr3.points();

        assertEquals(expected, actual);
    }

    /*
    @Test
    public void pointsTest3(){


        List<Route> lr = new ArrayList<>();
        List<Route> lr2 = new ArrayList<>();
        List<Route> lr3 = new ArrayList<>();

        List<Edge> le1 = new ArrayList<>();
        List<Edge> le2 = new ArrayList<>();
        List<Edge> le3 = new ArrayList<>();
        List<Edge> le4 = new ArrayList<>();
        List<Edge> le5 = new ArrayList<>();
        List<Edge> le6 = new ArrayList<>();


        Edge e1 = new Edge(0, 1, new PointCh(MIN_E , MIN_N ), new PointCh(MIN_E + 10, MIN_N),
                10, operand -> operand);
        Edge e2 = new Edge(1, 2, new PointCh(MIN_E + 10 , MIN_N ), new PointCh(MIN_E + 20, MIN_N),
                10, operand -> operand+1);

        le1.add(e1);
        le1.add(e2);

        Edge e3 = new Edge(2, 3, new PointCh(MIN_E + 20 , MIN_N ), new PointCh(MIN_E + 30, MIN_N),
                10, operand -> operand+2);
        Edge e4 = new Edge(3, 4, new PointCh(MIN_E + 30 , MIN_N ), new PointCh(MIN_E + 40, MIN_N),
                10, operand -> operand+3);

        le2.add(e3);
        le2.add(e4);

        Edge e5 = new Edge(2, 3, new PointCh(MIN_E + 40 , MIN_N ), new PointCh(MIN_E + 50, MIN_N),
                10, operand -> operand+4);
        Edge e6 = new Edge(3, 4, new PointCh(MIN_E + 50 , MIN_N ), new PointCh(MIN_E + 60, MIN_N),
                10, operand -> operand+5);

        le3.add(e5);
        le3.add(e6);

        Edge e7 = new Edge(3, 4, new PointCh(MIN_E + 60 , MIN_N ), new PointCh(MIN_E + 70, MIN_N),
                10, operand -> operand+6);
        Edge e8 = new Edge(4, 5, new PointCh(MIN_E + 70 , MIN_N ), new PointCh(MIN_E + 80, MIN_N),
                10, operand -> operand+7);

        le4.add(e7);
        le4.add(e8);

        Edge e9 = new Edge(4, 5, new PointCh(MIN_E + 80 , MIN_N ), new PointCh(MIN_E + 90, MIN_N),
                10, operand -> operand+8);
        Edge e10 = new Edge(5, 6, new PointCh(MIN_E + 90 , MIN_N ), new PointCh(MIN_E + 100, MIN_N),
                10, operand -> operand+9);

        le5.add(e9);
        le5.add(e10);

        Edge e11 = new Edge(6, 7, new PointCh(MIN_E + 100 , MIN_N ), new PointCh(MIN_E + 110, MIN_N),
                10, operand -> operand+10);
        Edge e12 = new Edge(7, 8, new PointCh(MIN_E + 110 , MIN_N ), new PointCh(MIN_E + 120, MIN_N),
                10, operand -> operand+11);

        le6.add(e11);
        le6.add(e12);


        lr.add(new SingleRoute(le1));
        lr.add(new SingleRoute(le2));
        lr.add(new SingleRoute(le3));
        lr2.add(new SingleRoute(le4));
        lr2.add(new SingleRoute(le5));
        lr2.add(new SingleRoute(le6));

        MultiRoute mr = new MultiRoute(lr);

        MultiRoute mr2 = new MultiRoute(lr2);

        lr3.add(mr);
        lr3.add(mr2);
        MultiRoute mr3 = new MultiRoute(lr3);

        List<PointCh> expected = new ArrayList<>();
        expected.add(new PointCh(MIN_E , MIN_N ));
        expected.add(new PointCh(MIN_E + 10 , MIN_N ));
        expected.add(new PointCh(MIN_E + 20 , MIN_N ));
        expected.add(new PointCh(MIN_E + 30 , MIN_N ));
        expected.add(new PointCh(MIN_E + 40 , MIN_N ));
        expected.add(new PointCh(MIN_E + 50 , MIN_N ));
        expected.add(new PointCh(MIN_E + 60 , MIN_N ));
        expected.add(new PointCh(MIN_E + 70 , MIN_N ));
        expected.add(new PointCh(MIN_E + 80 , MIN_N ));
        expected.add(new PointCh(MIN_E + 90 , MIN_N ));
        expected.add(new PointCh(MIN_E + 100 , MIN_N ));
        expected.add( new PointCh(MIN_E + 110 , MIN_N ));
        expected.add(new PointCh(MIN_E + 120, MIN_N));

        List<PointCh> actual = mr3.points();

        assertEquals(expected, actual);
    }

     */



    @Test
    private static List<Edge> verticalEdges(int edgesCount) {
        var edges = new ArrayList<Edge>(edgesCount);
        for (int i = 0; i < edgesCount; i += 1) {
            var p1 = new PointCh(ORIGIN_E, ORIGIN_N + i * EDGE_LENGTH);
            var p2 = new PointCh(ORIGIN_E, ORIGIN_N + (i + 1) * EDGE_LENGTH);
            edges.add(new Edge(i, i + 1, p1, p2, EDGE_LENGTH, x -> Double.NaN));
        }
        return Collections.unmodifiableList(edges);
    }

    private static PointCh sawToothPoint(int i) {
        return new PointCh(
                ORIGIN_E + TOOTH_EW * i,
                ORIGIN_N + ((i & 1) == 0 ? 0 : TOOTH_NS));
    }

    private static List<Edge> sawToothEdges(int edgesCount) {
        var edges = new ArrayList<Edge>(edgesCount);
        for (int i = 0; i < edgesCount; i += 1) {
            var p1 = sawToothPoint(i);
            var p2 = sawToothPoint(i + 1);
            var startingElevation = i * TOOTH_ELEVATION_GAIN;
            edges.add(new Edge(i, i + 1, p1, p2, TOOTH_LENGTH, x -> startingElevation + x * TOOTH_SLOPE));
        }
        return Collections.unmodifiableList(edges);
    }







}
