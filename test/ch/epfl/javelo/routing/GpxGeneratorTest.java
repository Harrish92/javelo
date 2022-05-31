package ch.epfl.javelo.routing;

import ch.epfl.javelo.data.Graph;
import ch.epfl.javelo.projection.Ch1903;
import ch.epfl.javelo.projection.PointCh;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.random.RandomGenerator;

import static ch.epfl.test.TestRandomizer.newRandom;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GpxGeneratorTest {
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
    public void createGpxTest(){
        var rng = newRandom();
        var route = new SingleRoute(createEpflSauvabelin());
        var length = Math.nextUp(rng.nextDouble(1000));
        var profile = new ElevationProfile(length, new float[]{1, 2, 3});
        GpxGenerator.createGpx(route, profile);

    }

    private static List<Edge> verticalEdges() {
        var edges = new ArrayList<Edge>(1);
        for (int i = 0; i < 1; i += 1) {
            var p1 = new PointCh(ORIGIN_E, ORIGIN_N + i * EDGE_LENGTH);
            var p2 = new PointCh(ORIGIN_E, ORIGIN_N + (i + 1) * EDGE_LENGTH);
            edges.add(new Edge(i, i + 1, p1, p2, EDGE_LENGTH, x -> Double.NaN));
        }
        return Collections.unmodifiableList(edges);
    }

    private static List<Edge> createEpflSauvabelin(){
        var edges = new ArrayList<Edge>();

        var p1 = new PointCh( Ch1903.e(Math.toRadians(6.5615820), Math.toRadians(46.5190582)),
                Ch1903.n(Math.toRadians(6.5615820), Math.toRadians(46.5190582))); // EPFL
        var p2 = new PointCh( Ch1903.e(Math.toRadians(7.0168378), Math.toRadians(47.4925267)),
                Ch1903.n(Math.toRadians(7.0168378), Math.toRadians(47.4925267))); // Sauvabelin
        edges.add(new Edge(159049, 117669, p1, p2, p1.distanceTo(p2), x -> Double.NaN));
        return Collections.unmodifiableList(edges);

    }

    private static float[] randomSamples(RandomGenerator rng, int count) {
        var samples = new float[count];
        for (int i = 0; i < count; i += 1)
            samples[i] = rng.nextFloat(4096);
        return samples;
    }

    @Test
    public void writeGpxTest() throws IOException {
        var rng = newRandom();
        Graph g = Graph.loadFrom(Path.of("lausanne"));
        CostFunction cf = new CityBikeCF(g);
        RouteComputer rc = new RouteComputer(g, cf);
        Route route = rc.bestRouteBetween(159049, 117669);
        var sampleCount = rng.nextInt(2, 1000);
        var profile = ElevationProfileComputer.elevationProfile(route, 5);

        GpxGenerator.writeGpx("fi", route, profile);
    }
}
