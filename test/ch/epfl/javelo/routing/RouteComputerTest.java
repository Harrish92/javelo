package ch.epfl.javelo.routing;

import ch.epfl.javelo.data.Graph;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class RouteComputerTest {

    @Test
    void nullTest() throws IOException {
        Graph g = Graph.loadFrom(Path.of("lausanne"));
        CostFunction cf = new CityBikeCF(g);
        RouteComputer rc = new RouteComputer(g, cf);
        /*Route r = rc.bestRouteBetween(159049, 159049);
        assertNull(r);*/ //TODO: route null?
    }

    @Test
    void testProf() throws IOException {
        Graph g = Graph.loadFrom(Path.of("lausanne"));
        CostFunction cf = new CityBikeCF(g);
        RouteComputer rc = new RouteComputer(g, cf);
        Route r = rc.bestRouteBetween(159049, 117669);
        KmlPrinter.write("javelo.kml", r);
    }


}