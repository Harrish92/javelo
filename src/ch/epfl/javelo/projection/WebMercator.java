package ch.epfl.javelo.projection;

import ch.epfl.javelo.Math2;

/**
 * @author Harrishan Raveendran (345291)
 */
public class WebMercator {

    /**
     *
     * @param lon longitude
     * @return la cordoonnée x.
     */
    public static double x(double lon){
        return 1/(2*Math.PI) * (lon + Math.PI);
    }

    /**
     *
     * @param lat latitude
     * @return la cordoonnée y.
     */
    public static double y(double lat){
        return (Math.PI - Math2.asinh(Math.tan(lat))) / (2*Math.PI);
    }

    /**
     *
     * @param x coordonnée x
     * @return la longitude.
     */
    public static double lon(double x){
        return 2*Math.PI*x-Math.PI;
    }

    /**
     *
     * @param y coordonnée y
     * @return la latitude.
     */
    public static double lat(double y){
        return Math.atan(Math.sinh(Math.PI - 2*Math.PI*y));
    }

}
