package ch.epfl.javelo.projection;

import ch.epfl.javelo.Math2;

/**
 * @author Harrishan Raveendran (345291)
 */
public class WebMercator {

    /**
     *
     * @param lon
     * @return retourne la lon
     */
    static double x(double lon){
        return 1/(2*Math.PI) * (lon + Math.PI);
    }

    static double y(double lat){
        return 1/(2*Math.PI)*(Math.PI - Math2.asinh(Math.tan(lat)));
    }

    static double lon(double x){
        return 2*Math.PI*x-Math.PI;
    }

    static double lat(double y){
        return Math.atan(Math.sinh(Math.PI - 2*Math.PI*y));
    }

}
