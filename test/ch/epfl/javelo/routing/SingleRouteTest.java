package ch.epfl.javelo.routing;

import ch.epfl.javelo.projection.PointCh;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import org.junit.runner.notification.RunListener;

import java.awt.*;
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

    @Test
    public void pointsTest(){
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0, 1, new PointCh(2485000, 1075000), new PointCh(2485001, 1075000), 1, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2485001, 1075000), new PointCh(2485002, 1075000), 2, operand -> operand*operand));
        SingleRoute sr = new SingleRoute(edges);

        List<PointCh> expected = new ArrayList<>();
        expected.add(new PointCh(2485000, 1075000));
        expected.add(new PointCh(2485001, 1075000));
        expected.add(new PointCh(2485002, 1075000));
        List<PointCh> actual = sr.points();
        assertEquals(expected, actual);
    }

    @Test
    public void pointAtTestExactlyOnNode(){
        List<Edge> edges = new ArrayList<>();
        PointCh expected = new PointCh(2493100, 1075000);

        edges.add(new Edge(0, 1, new PointCh(2485000, 1075000), new PointCh(2490800, 1075000), 5800, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2490800, 1075000),new PointCh(2493100, 1075000), 2300, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2493100, 1075000), new PointCh(2494200, 1075000), 1100, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2494200, 1075000), new PointCh(2496400, 1075000), 2200, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2498600, 1075000), new PointCh(2500300, 1075000), 1700, operand -> operand*operand));

        SingleRoute sr = new SingleRoute(edges);

        double position = 8100;
        PointCh actual = sr.pointAt(position);
        assertEquals(expected, actual);
    }

    @Test
    public void pointAtTestExactlyOnNodePos0(){
        List<Edge> edges = new ArrayList<>();
        PointCh expected = new PointCh(2485000, 1075000);

        edges.add(new Edge(0, 1, new PointCh(2485000, 1075000), new PointCh(2490800, 1075000), 5800, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2490800, 1075000),new PointCh(2493100, 1075000), 2300, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2493100, 1075000), new PointCh(2494200, 1075000), 1100, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2494200, 1075000), new PointCh(2496400, 1075000), 2200, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2498600, 1075000), new PointCh(2500300, 1075000), 1700, operand -> operand*operand));

        SingleRoute sr = new SingleRoute(edges);

        double position = 0;

        PointCh actual = sr.pointAt(position);
        assertEquals(expected, actual);
    }

    @Test
    public void pointAtTestExactlyOnLastNode(){
        List<Edge> edges = new ArrayList<>();
        PointCh expected = new PointCh(2500300, 1075000);

        edges.add(new Edge(0, 1, new PointCh(2485000, 1075000), new PointCh(2490800, 1075000), 5800, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2490800, 1075000),new PointCh(2493100, 1075000), 2300, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2493100, 1075000), new PointCh(2494200, 1075000), 1100, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2494200, 1075000), new PointCh(2496400, 1075000), 2200, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2498600, 1075000), new PointCh(2500300, 1075000), 1700, operand -> operand*operand));

        SingleRoute sr = new SingleRoute(edges);

        double position = sr.length();

        PointCh actual = sr.pointAt(position);
        assertEquals(expected, actual);
    }

    @Test
    public void pointAtTestExactlyOnRandomNode(){
        List<Edge> edges = new ArrayList<>();
        int[] random_array = {0, 5800, 8100, 9200, 11400, 13100};
        PointCh[] pch_array = {
                new PointCh(2485000, 1075000),
                new PointCh(2490800, 1075000),
                new PointCh(2493100, 1075000),
                new PointCh(2494200, 1075000),
                new PointCh(2496400, 1075000),
                new PointCh(2498100, 1075000)

        };

        int Random_index = (int) (Math.random() * random_array.length);
        System.out.println(Random_index);
        double position = random_array[Random_index];
        PointCh expected = pch_array[Random_index];

        edges.add(new Edge(0, 1, new PointCh(2485000, 1075000),
                new PointCh(2490800, 1075000), 5800, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2490800, 1075000),
                new PointCh(2493100, 1075000), 2300, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2493100, 1075000),
                new PointCh(2494200, 1075000), 1100, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2494200, 1075000),
                new PointCh(2496400, 1075000), 2200, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2496400, 1075000),
                new PointCh(2498100, 1075000), 1700, operand -> operand*operand));

        SingleRoute sr = new SingleRoute(edges);

        PointCh actual = sr.pointAt(position);
        assertEquals(expected, actual);
    }

    @Test
    public void pointAtTestInBetweenNodes1(){
        List<Edge> edges = new ArrayList<>();
        PointCh expected = new PointCh(2487900, 1075000);

        edges.add(new Edge(0, 1, new PointCh(2485000, 1075000),
                new PointCh(2490800, 1075000), 5800, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2490800, 1075000),
                new PointCh(2493100, 1075000), 2300, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2493100, 1075000),
                new PointCh(2494200, 1075000), 1100, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2494200, 1075000),
                new PointCh(2496400, 1075000), 2200, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2496400, 1075000),
                new PointCh(2498100, 1075000), 1700, operand -> operand*operand));

        SingleRoute sr = new SingleRoute(edges);

        double position = 2900;
        PointCh actual = sr.pointAt(position);
        assertEquals(expected, actual);
    }

    @Test
    public void pointAtTestInBetweenNodes2(){
        List<Edge> edges = new ArrayList<>();
        PointCh expected = new PointCh(2491950, 1075000);

        edges.add(new Edge(0, 1, new PointCh(2485000, 1075000),
                new PointCh(2490800, 1075000), 5800, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2490800, 1075000),
                new PointCh(2493100, 1075000), 2300, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2493100, 1075000),
                new PointCh(2494200, 1075000), 1100, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2494200, 1075000),
                new PointCh(2496400, 1075000), 2200, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2496400, 1075000),
                new PointCh(2498100, 1075000), 1700, operand -> operand*operand));

        SingleRoute sr = new SingleRoute(edges);

        double position = 6950;
        PointCh actual = sr.pointAt(position);
        assertEquals(expected, actual);
    }

    @Test
    public void pointAtTestInBetweenNodes3(){
        List<Edge> edges = new ArrayList<>();
        PointCh expected = new PointCh(2497250, 1075000);

        edges.add(new Edge(0, 1, new PointCh(2485000, 1075000),
                new PointCh(2490800, 1075000), 5800, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2490800, 1075000),
                new PointCh(2493100, 1075000), 2300, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2493100, 1075000),
                new PointCh(2494200, 1075000), 1100, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2494200, 1075000),
                new PointCh(2496400, 1075000), 2200, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2496400, 1075000),
                new PointCh(2498100, 1075000), 1700, operand -> operand*operand));

        SingleRoute sr = new SingleRoute(edges);

        double position = 12250;
        PointCh actual = sr.pointAt(position);
        assertEquals(expected, actual);
    }

    @Test
    public void pointAtTestRandomInBetweenNodes(){
        double [] tab = {0, 5800, 8100, 9200, 11400, 13100};
        List<Edge> edges = new ArrayList<>();
        double position = Math.random() * 13100;
        int index = 0;
        System.out.println(position);

        if(position >= 5800 && position <= 8100)
            index = 1;
        if(position >= 8100 && position <= 9200)
            index = 2;
        if(position >= 9200 && position <= 11400)
            index = 3;
        if(position >= 11400 && position <= 13100)
            index = 4;
        edges.add(new Edge(0, 1, new PointCh(2485000, 1075000),
                new PointCh(2490800, 1075000), 5800, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2490800, 1075000),
                new PointCh(2493100, 1075000), 2300, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2493100, 1075000),
                new PointCh(2494200, 1075000), 1100, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2494200, 1075000),
                new PointCh(2496400, 1075000), 2200, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2496400, 1075000),
                new PointCh(2498100, 1075000), 1700, operand -> operand*operand));


        SingleRoute sr = new SingleRoute(edges);

        PointCh actual = sr.pointAt(position);

        position-= tab[index];

        PointCh expexted = sr.edges().get(index).pointAt(position);


        assertEquals(expexted, actual);
    }

    @Test
    public void pointAtTestRandomInBetweenNodes10times(){
        for(int i=0; i < 10; i++){
            pointAtTestRandomInBetweenNodes();
        }
    }


        @Test
    public void pointClosestToTest1(){
        List<Edge> edges = new ArrayList<>();

        edges.add(new Edge(0, 1, new PointCh(2485000, 1075000), new PointCh(2490800, 1075000), 5800, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2490800, 1075000),new PointCh(2493100, 1075000), 2300, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2493100, 1075000), new PointCh(2494200, 1075000), 1100, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2494200, 1075000), new PointCh(2496400, 1075000), 2200, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2498600, 1075000), new PointCh(2500300, 1075000), 1700, operand -> operand*operand));

        SingleRoute sr = new SingleRoute(edges);

        PointCh pch = new PointCh(2490800, 1075000);

        RoutePoint expected = new RoutePoint(new PointCh(2490800, 1075000), 5800, 0);

        RoutePoint actual = sr.pointClosestTo(pch);
        assertEquals(expected, actual);

    }

    @Test
    public void pointClosestToTest2(){
        List<Edge> edges = new ArrayList<>();

        edges.add(new Edge(0, 1, new PointCh(2485000, 1075000), new PointCh(2490800, 1075000), 5800, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2490800, 1075000),new PointCh(2493100, 1075000), 2300, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2493100, 1075000), new PointCh(2494200, 1075000), 1100, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2494200, 1075000), new PointCh(2496400, 1075000), 2200, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2496400, 1075000), new PointCh(2500300, 1075000), 1700, operand -> operand*operand));

        SingleRoute sr = new SingleRoute(edges);

        PointCh pch = new PointCh(2496400, 1075452);

        RoutePoint expected = new RoutePoint(new PointCh(2496400, 1075000), 11400, 452);

        RoutePoint actual = sr.pointClosestTo(pch);
        assertEquals(expected, actual);

    }

    @Test
    public void clampIsWorking1(){
        List<Edge> edges = new ArrayList<>();
        PointCh expected = new PointCh(2485000, 1075000);

        edges.add(new Edge(0, 1, new PointCh(2485000, 1075000), new PointCh(2490800, 1075000), 5800, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2490800, 1075000),new PointCh(2493100, 1075000), 2300, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2493100, 1075000), new PointCh(2494200, 1075000), 1100, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2494200, 1075000), new PointCh(2496400, 1075000), 2200, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2498600, 1075000), new PointCh(2500300, 1075000), 1700, operand -> operand*operand));

        SingleRoute sr = new SingleRoute(edges);

        double position = -1;

        PointCh actual = sr.pointAt(position);
        assertEquals(expected, actual);

    }

    @Test
    public void clampIsWorking2(){
        List<Edge> edges = new ArrayList<>();
        PointCh expected = new PointCh(2500300, 1075000);

        edges.add(new Edge(0, 1, new PointCh(2485000, 1075000), new PointCh(2490800, 1075000), 5800, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2490800, 1075000),new PointCh(2493100, 1075000), 2300, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2493100, 1075000), new PointCh(2494200, 1075000), 1100, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2494200, 1075000), new PointCh(2496400, 1075000), 2200, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2498600, 1075000), new PointCh(2500300, 1075000), 1700, operand -> operand*operand));

        SingleRoute sr = new SingleRoute(edges);

        double position = Double.POSITIVE_INFINITY;

        PointCh actual = sr.pointAt(position);
        assertEquals(expected, actual);

    }

    @Test
    public void emptyListThrowsIllegalArgument(){
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            List<Edge> edges = new ArrayList<>();
            SingleRoute sr = new SingleRoute(edges);
        });

    }

    @Test
    public void elevationAtTest1(){
        List<Edge> edges = new ArrayList<>();

        edges.add(new Edge(0, 1, new PointCh(2485000, 1075000), new PointCh(2490800, 1075000), 5800, operand -> operand*1));
        edges.add(new Edge(1, 2, new PointCh(2490800, 1075000),new PointCh(2493100, 1075000), 2300, operand -> operand*2));
        edges.add(new Edge(1, 2, new PointCh(2493100, 1075000), new PointCh(2494200, 1075000), 1100, operand -> operand*3));
        edges.add(new Edge(1, 2, new PointCh(2494200, 1075000), new PointCh(2496400, 1075000), 2200, operand -> operand*4));
        edges.add(new Edge(1, 2, new PointCh(2498600, 1075000), new PointCh(2500300, 1075000), 1700, operand -> operand*operand));

        SingleRoute sr = new SingleRoute(edges);

        double position = sr.length();
        double actual = sr.elevationAt(position);
        double expected = 2890000;
        assertEquals(expected, actual);
    }

    @Test
    public void elevationAtTest2(){
        List<Edge> edges = new ArrayList<>();

        edges.add(new Edge(0, 1, new PointCh(2485000, 1075000), new PointCh(2490800, 1075000), 5800, operand -> operand*1));
        edges.add(new Edge(1, 2, new PointCh(2490800, 1075000),new PointCh(2493100, 1075000), 2300, operand -> operand*2));
        edges.add(new Edge(1, 2, new PointCh(2493100, 1075000), new PointCh(2494200, 1075000), 1100, operand -> operand*3));
        edges.add(new Edge(1, 2, new PointCh(2494200, 1075000), new PointCh(2496400, 1075000), 2200, operand -> operand*4));
        edges.add(new Edge(1, 2, new PointCh(2498600, 1075000), new PointCh(2500300, 1075000), 1700, operand -> operand*operand));

        SingleRoute sr = new SingleRoute(edges);

        double position = 0;
        double actual = sr.elevationAt(position);
        double expected = 0;
        assertEquals(expected, actual);

    }

    @Test
    public void ElevationAtTestRandomInBetweenNodes(){
        double [] tab = {0, 5800, 8100, 9200, 11400, 13100};
        List<Edge> edges = new ArrayList<>();
        double position = Math.random() * 13100;
        double dummy = position;

        double expected = position;

        if(position >= 5800 && position <= 8100) {
            position -= tab[1];
            expected = position*2;
        }
        if(position >= 8100 && position <= 9200) {
            position -= tab[2];
            expected = position*3;
        }
        if(position >= 9200 && position <= 11400) {
            position -= tab[3];
            expected = position*4;
        }
        if(position >= 11400 && position <= 13100) {
            position -= tab[4];
            expected = position*5;
        }
        edges.add(new Edge(0, 1, new PointCh(2485000, 1075000),
                new PointCh(2490800, 1075000), 5800, operand -> operand*1));
        edges.add(new Edge(1, 2, new PointCh(2490800, 1075000),
                new PointCh(2493100, 1075000), 2300, operand -> operand*2));
        edges.add(new Edge(1, 2, new PointCh(2493100, 1075000),
                new PointCh(2494200, 1075000), 1100, operand -> operand*3));
        edges.add(new Edge(1, 2, new PointCh(2494200, 1075000),
                new PointCh(2496400, 1075000), 2200, operand -> operand*4));
        edges.add(new Edge(1, 2, new PointCh(2496400, 1075000),
                new PointCh(2496400, 1075000), 1700, operand -> operand*5));

        SingleRoute sr = new SingleRoute(edges);
        double actual = sr.elevationAt(dummy);


        assertEquals(expected, actual);
    }

    @Test
    public void ElevationAtRandom10Times(){
        for(int i=0; i < 10; i++){
            ElevationAtTestRandomInBetweenNodes();
        }
    }

    @Test
    public void nodeClosestToTestForNode0(){
        List<Edge> edges = new ArrayList<>();

        edges.add(new Edge(0, 1, new PointCh(2485000, 1075000), new PointCh(2490800, 1075000), 5800, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2490800, 1075000),new PointCh(2493100, 1075000), 2300, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2493100, 1075000), new PointCh(2494200, 1075000), 1100, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2494200, 1075000), new PointCh(2496400, 1075000), 2200, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2496400, 1075000), new PointCh(2500300, 1075000), 1700, operand -> operand*operand));

        SingleRoute sr = new SingleRoute(edges);

        double position = 0;
        int expected = 0;
        int actual = sr.nodeClosestTo(position);
        assertEquals(expected, actual);

    }

    @Test
    public void nodeClosestToTestForLastNode(){
        List<Edge> edges = new ArrayList<>();

        edges.add(new Edge(0, 1, new PointCh(2485000, 1075000), new PointCh(2490800, 1075000), 5800, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2490800, 1075000),new PointCh(2493100, 1075000), 2300, operand -> operand*operand));
        edges.add(new Edge(2, 3, new PointCh(2493100, 1075000), new PointCh(2494200, 1075000), 1100, operand -> operand*operand));
        edges.add(new Edge(3, 4, new PointCh(2494200, 1075000), new PointCh(2496400, 1075000), 2200, operand -> operand*operand));
        edges.add(new Edge(4, 5, new PointCh(2496400, 1075000), new PointCh(2500300, 1075000), 1700, operand -> operand*operand));

        SingleRoute sr = new SingleRoute(edges);

        double position = 13100;
        int expected = 5;
        int actual = sr.nodeClosestTo(position);
        assertEquals(expected, actual);
    }

    @Test
    public void nodeClosestToRandomTest(){
        List<Edge> edges = new ArrayList<>();
        double position = Math.random() * 13100;
        int expected = 0;

        if(position > 2900){
            expected = 1;
        }

        if(position >= 5800 && position <= 8100) {
            if(position < 6950){
                expected = 1;
            }else{
                expected = 2;
            }
        }
        if(position >= 8100 && position <= 9200) {
            if(position < 8650){
                expected = 2;
            }else{
                expected = 3;
            }
        }
        if(position >= 9200 && position <= 11400) {
            if(position < 10300){
                expected = 3;
            }else{
                expected = 4;
            }
        }
        if(position >= 11400 && position <= 13100) {
            if(position < 12250){
                expected = 4;
            }else{
                expected = 5;
            }
        }


        edges.add(new Edge(0, 1, new PointCh(2485000, 1075000), new PointCh(2490800, 1075000), 5800, operand -> operand*operand));
        edges.add(new Edge(1, 2, new PointCh(2490800, 1075000),new PointCh(2493100, 1075000), 2300, operand -> operand*operand));
        edges.add(new Edge(2, 3, new PointCh(2493100, 1075000), new PointCh(2494200, 1075000), 1100, operand -> operand*operand));
        edges.add(new Edge(3, 4, new PointCh(2494200, 1075000), new PointCh(2496400, 1075000), 2200, operand -> operand*operand));
        edges.add(new Edge(4, 5, new PointCh(2496400, 1075000), new PointCh(2500300, 1075000), 1700, operand -> operand*operand));

        SingleRoute sr = new SingleRoute(edges);

        int actual = sr.nodeClosestTo(position);
        assertEquals(expected, actual);
    }

    @Test
    public void nodeClosestToRandomTest10Times(){
        for(int i=0; i < 10; i++){
            nodeClosestToRandomTest();
        }
    }









}
