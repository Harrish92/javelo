package ch.epfl.javelo.routing;

import ch.epfl.javelo.Math2;
import ch.epfl.javelo.Preconditions;

import java.util.Arrays;

/**
 * ElevationProfileCompute génére un profil à partir d'une route.
 *
 * @author Yoan Giovannini (303934)
 */
public final class ElevationProfileComputer {

    //Classe non instanciable
    private ElevationProfileComputer() {
    }

    /**
     * Calcule le profile en long de l'itinéraire route avec espacement maximal
     * entre les échantillons de maxStepLength.
     *
     * @param route         l'itinéraire.
     * @param maxStepLength la distance maximale entre deux échantillons.
     * @return le profile d'un itinéraire.
     */
    public static ElevationProfile elevationProfile(Route route, double maxStepLength) {
        Preconditions.checkArgument(maxStepLength > 0);
        int nbPoints = (int) (Math.ceil(route.length() / maxStepLength)) + 1;
        double stepLength = route.length() / nbPoints;
        float[] points = new float[nbPoints];

        //phase 0 : remplissage du tableau des échantillons

        for (int i = 0; i < nbPoints; i++) {
            double position = Math.ceil(i * stepLength);
            points[i] = (float) route.elevationAt(position);
        }
        //Phases 1, 2, 3 permettent de remplir les <<trous>> dans le tableau des échantillons,
        //en remplaçant les valeurs NaN qu'il contient par des valeurs interpolées.

        //phase 1 : rechercher le premier échantillon non vide du tableau, et l'utiliser pour boucher les <<trous>>

        int j = 0;

        while (Float.isNaN(points[j]) && j < points.length - 1) {
            j++;
        }
        if (Float.isNaN(points[j])) {
            points[j] = 0;
        }
        Arrays.fill(points, 0, j, points[j]);

        //phase 2

        int k = points.length - 1;

        while (Float.isNaN(points[k]) && k > 0) {
            k--;
        }
        Arrays.fill(points, k, points.length, points[k]);

        //phase 3

        float bg, bd;
        int c;

        for (int l = j + 1; l <= k; l++) {
            if (Float.isNaN(points[l])) {
                bg = points[l - 1];
                c = 1;
                while (Float.isNaN(points[l])) {
                    ++l;
                    ++c;
                }
                bd = points[l];
                for (int d = 1; d <= c - 1; ++d) {
                    points[l - c + d] = (float) Math2.interpolate(bg, bd, (double) d / c);
                }
            }
        }
        return new ElevationProfile(route.length(), points);
    }


}
