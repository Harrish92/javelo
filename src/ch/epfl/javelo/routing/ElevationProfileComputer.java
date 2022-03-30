package ch.epfl.javelo.routing;

import ch.epfl.javelo.Math2;
import ch.epfl.javelo.Preconditions;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * //TODO: commenter
 */
public final class ElevationProfileComputer {

    //Classe non instanciable
    private ElevationProfileComputer() {}

    /**
     * Calcule le profile en long de l'itinéraire route avec espacement maximal
     * entre les échantillons de maxStepLength.
     * @param route l'itinéraire.
     * @param maxStepLength la distance maximale entre deux échantillons.
     * @return le profile d'un itinéraire.
     */
    public static ElevationProfile elevationProfile(Route route, double maxStepLength) {
        Preconditions.checkArgument(maxStepLength > 0);
        int nbPoints = (int) (Math.ceil(route.length() / maxStepLength)) + 1;
        double stepLength = route.length()/nbPoints;
        //test
        //nbPoints = 2;
        float[] points = new float[nbPoints];
        for(int i = 0; i < nbPoints; i++) {
            double position = Math.ceil(i*stepLength);
            points[i] = (float) route.elevationAt(position);
        }
        //phase 1
        int j = 0;
        while(Float.isNaN(points[j]) && j < points.length - 1) {j++;}
        if(Float.isNaN(points[j])){points[j] = 0;}
        Arrays.fill(points, 0, j, points[j]);
        //phase 2
        int k = points.length - 1;
        while (Float.isNaN(points[k]) && k > 0){k--;}
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
        return new ElevationProfile(route.length(), points);
    }



}
