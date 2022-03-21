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

    public static ElevationProfile elevationProfile(Route route, double maxStepLength) {
        Preconditions.checkArgument(maxStepLength > 0);
        int nbPoints = (int) (Math.ceil(route.length() / maxStepLength) + 1);
        float[] points = new float[nbPoints];
        for(int i = 0; i < nbPoints; i++) {
            points[i] = (float) route.elevationAt(i*maxStepLength);//TODO: verifier
        }
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
                //+que 1 trou
                for(int d = 1; d < c; ++d){
                    points[l - c + d] = (float) Math2.interpolate(bg, bd, d/(c-1));//TODO: verifier
                }
            }
        }
        return new ElevationProfile(nbPoints*maxStepLength, points);
    }



}
