package ch.epfl.javelo.routing;

import ch.epfl.javelo.Math2;
import ch.epfl.javelo.Preconditions;
import ch.epfl.javelo.data.Graph;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ElevationProfileComputerTest {


    @Test
    void ElevationProfileComputerOneEdge() throws IOException {
        ArrayList<Edge> liste = new ArrayList<>();
        Graph graph = Graph.loadFrom(Path.of("lausanne"));
        liste.add(Edge.of(graph, graph.nodeOutEdgeId(2033, 0),2033,2034));
        SingleRoute route = new SingleRoute(liste);
        ElevationProfile profile = ElevationProfileComputer.elevationProfile(route, 2);
        System.out.println(profile.totalAscent());
        assertEquals(liste.get(0).length(), profile.length(), 1e-3);
        for(int i = -1; i < 40; i++) {
            assertEquals(route.elevationAt(i), profile.elevationAt(i), 1e-2);
        }
    }

    @Test
    void ElevationProfileComputerMultipleEdges() throws IOException {
        ArrayList<Edge> liste = new ArrayList<>();
        Graph graph = Graph.loadFrom(Path.of("lausanne"));
        for (int i = 0; i < 10; i++) {
            liste.add(Edge.of(graph, graph.nodeOutEdgeId(2033+i, 0),2033+i,2034+i));
        }
        SingleRoute route = new SingleRoute(liste);
        ElevationProfile profile = ElevationProfileComputer.elevationProfile(route, 2);
        System.out.println(profile.totalAscent());
        assertEquals(route.length(), profile.length(), 1e-3);
        for(int i = -1; i < 300; i++) {
            assertEquals(route.elevationAt(i), profile.elevationAt(i), 9e-1);
        }
    }

    @Test
    void ElevationProfileComputerError() {
        assertThrows(IllegalArgumentException.class, () -> ElevationProfileComputer.elevationProfile(null, 0));
        assertThrows(IllegalArgumentException.class, () -> ElevationProfileComputer.elevationProfile(null, -1));
    }

    @Test
    void ElevationProfileComputerSimplified() {
        float[] l = {1,2,3,4,5,6,7,8};
        assertArrayEquals(l, elevationProfileT(l, 1));
        l[0] = Float.NaN;
        l[1] = Float.NaN;
        l[2] = Float.NaN;
        l[7] = Float.NaN;
        //assertArrayEquals(new float[] {4,4,4,4,5,6,7,7}, elevationProfileT(l, 1));
        l[4] = Float.NaN;
        l[5] = Float.NaN;
        System.out.println(Math2.interpolate(4, 7, 1/2.0));
        assertArrayEquals(new float[] {4,4,4,4,5,6,7,7}, elevationProfileT(l, 1));
    }

    public static float[] elevationProfileT(float[] liste, double maxStepLength) {
        Preconditions.checkArgument(maxStepLength > 0);
        //int nbPoints = (int) (Math.ceil(liste.length / maxStepLength)) + 1;
        float[] points = liste;
        //phase 1
        int j = 0;
        while(Float.isNaN(points[j])) {j++;}
        Arrays.fill(points, 0, j, points[j]);
        //phase 2
        int k = points.length - 1;
        while (Float.isNaN(points[k])){k--;}
        Arrays.fill(points, k, points.length, points[k]);
        //phase 3
        float bg = 0;
        float bd = 0;
        int c = 0;
        for(int l = j + 1; l <= k; l++){
            if(Float.isNaN(points[l])){
                bg = points[l-1];
                c = 1;
                while (Float.isNaN(points[l])){++l;++c;}
                bd = points[l];
                /*Arrays.fill(points,l-c,l-1, );
                Math2.interpolate(bg, bd, 1/(c+2));*/
                //plus que 1 trou ?
                for (int d = 1; d <= c - 1; ++d) {
                    points[l - c + d] = (float) Math2.interpolate(bg, bd, (double)d / c);
                }
            }
        }
        return points;
    }
    //TTT
    @Test
    void elevationProfileComputerWorksOnArrays() throws IOException {

        Graph graph = Graph.loadFrom(Path.of("lausanne"));
        //Route route = new ClasseImplementantRoute(Edge.of(graph, graph.nodeOutEdgeId(2022,0),2022,2023));
        float[] expected1 = {1, 2, 3, 4, 5};
        float[] tableau1 = {1, 2, 3, Float.NaN, 5};
        float[] expected2 = {1, 2, 3, 4, 5};
        float[] tableau2 = {1, 2, Float.NaN, Float.NaN, 5};
        float[] expected3 = {1, 2, 3, 4, 4};
        float[] tableau3 = {1, 2, 3, 4, Float.NaN};
        float[] expected4 = {2, 2, 3, 4, 5};
        float[] tableau4 = {Float.NaN, 2, 3, 4, 5};
        float[] expected5= {3,3,3,10.5f,18,19.33f,20.66f,22,23,23,23,23};
        float[] tableau5 = {Float.NaN,Float.NaN,3,Float.NaN, 18, Float.NaN, Float.NaN, 22,23, Float.NaN,Float.NaN,Float.NaN};
        assertArrayEquals(expected1, elevationTableau(tableau1));
        assertArrayEquals(expected2, elevationTableau(tableau2));
        assertArrayEquals(expected3, elevationTableau(tableau3));
        assertArrayEquals(expected4, elevationTableau(tableau4));

        assertEquals(3, nbSamples(15,8));

    }

    private float[] elevationTableau(float[] tableau) {
        float[] elevationSamples = tableau;
        int nbPoints = 5; //faut définir ici le nb de samples
        //le code de ta méthode pour remplir le tableau/interpoler
        float[] points = new float[nbPoints];
        double stepLength = 1;
        points = tableau;
        //phase 1
        int j = 0;
        while(Float.isNaN(points[j])) {j++;}
        Arrays.fill(points, 0, j, points[j]);
        //phase 2
        int k = points.length - 1;
        while (Float.isNaN(points[k])){k--;}
        Arrays.fill(points, k, points.length, points[k]);
        //phase 3
        float bg = 0;
        float bd = 0;
        int c = 0;
        for(int l = j + 1; l <= k; l++){
            if(Float.isNaN(points[l])){
                bg = points[l-1];
                c = 1;
                while (Float.isNaN(points[l])){++l;++c;}
                bd = points[l];
                /*Arrays.fill(points,l-c,l-1, );
                Math2.interpolate(bg, bd, 1/(c+2));*/
                //plus que 1 trou ?
                for (int d = 1; d <= c - 1; ++d) {
                    points[l - c + d] = (float) Math2.interpolate(bg, bd, (double) d / c);
                }
            }
        }
        return points;
    }

    private int nbSamples(double length, double maxStepLength){
        //ton calcul de nbSamples dans la classe
        int nbSamples = (int)Math.ceil(length/maxStepLength)+1;
        return nbSamples;
    }
}
