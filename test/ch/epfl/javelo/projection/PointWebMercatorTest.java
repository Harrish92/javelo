package ch.epfl.javelo.projection;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointWebMercatorTest {
    private static final double DELTA = 1e-7;


    @Test
    void errorTest(){
        assertThrows(IllegalArgumentException.class, () -> new PointWebMercator(0,-0.1));
        assertThrows(IllegalArgumentException.class, () -> new PointWebMercator(-0.2,0));
        assertThrows(IllegalArgumentException.class, () -> new PointWebMercator(-0.3,-0.4));
        assertThrows(IllegalArgumentException.class, () -> new PointWebMercator(1,1.01));
        assertThrows(IllegalArgumentException.class, () -> new PointWebMercator(1.2,1));
        assertThrows(IllegalArgumentException.class, () -> new PointWebMercator(2,-0.1));
    }

    @Test
    public void ofTest(){
        PointWebMercator Pwm = new PointWebMercator(0.518275214444, 0.353664894749);
        int zoomlevel = 2;
        double xzoom2 = Pwm.xAtZoomLevel(zoomlevel); // tests sur méthode effectué au préalable
        double yzoom2 = Pwm.yAtZoomLevel(zoomlevel); // tests sur méthode effectué au préalable
        PointWebMercator PwmZoom2 = PointWebMercator.of(zoomlevel, xzoom2, yzoom2);
        assertEquals(Pwm, PwmZoom2);



    }

    @Test
    public void ofPointChTest(){
        PointCh pCH = new PointCh(2485000, 1075000);
        PointWebMercator actual = PointWebMercator.ofPointCh(pCH);
        double lon = pCH.lon();
        double lat = pCH.lat();
        double x = WebMercator.x(lon);
        double y = WebMercator.y(lat);
        PointWebMercator expected = new PointWebMercator(x, y);
        assertEquals(expected, actual);

    }

    @Test
    public void xAtZoomLevelTest(){
        PointWebMercator Pwm = new PointWebMercator(0.518275214444, 0.353664894749);
        double actual = Pwm.xAtZoomLevel(19);
        double expected = Math.scalb(0.518275214444, 27);
        assertEquals(expected, actual, DELTA);
    }

    @Test
    public void yAtZoomLevelTest(){
        PointWebMercator Pwm = new PointWebMercator(0.518275214444, 0.353664894749);
        double actual = Pwm.yAtZoomLevel(19);
        double expected = Math.scalb(0.353664894749, 27);
        assertEquals(expected, actual);

    }

    @Test
    public void lonTest(){
        PointWebMercator Pwm = new PointWebMercator(0.5, 0.3);
        var expected1 = 0;
        var actual1 = Pwm.lon();
        assertEquals(expected1, actual1, DELTA);

    }

    @Test
    public void latTest(){
        PointWebMercator Pwm = new PointWebMercator(0.5, 0.3);
        var expected1 = 1.016240336;
        var actual1 = Pwm.lat();
        assertEquals(expected1, actual1, DELTA);

    }

    @Test
    public void toPointChTest(){
        double lon = Ch1903.lon(1, 1);   // e and y out of bound
        double lat = Ch1903.lat(1, 1);
        double x = WebMercator.x(lon);
        double y = WebMercator.y(lat);
        PointWebMercator Pwm = new PointWebMercator(x, y);
        var actual = Pwm.toPointCh();
        assertNull(actual);

    }



}