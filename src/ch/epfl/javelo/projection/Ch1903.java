package ch.epfl.javelo.projection;

/**
 *
 *
 * @author Harrishan Raveendran (345291)
 */

public final class Ch1903 {

    /**
     *
     * @param lon longitude
     * @param lat latitude
     * @return retourne la coordonnée E (est) du point de longitude lon et latitude lat dans le système WGS84.
     */
    public static double e(double lon, double lat){
        lon = Math.toDegrees(lon);
        lat = Math.toDegrees(lat);
        double lon1 = Math.pow(10, -4) * (3600 * lon - 26782.5);
        double lat1 = Math.pow(10, -4) * (3600 * lat - 169028.66);
        double e = 2600072.37
                + 211455.93*lon1
                - 10938.51 * lon1 * lat1
                - 0.36 * lon1 * Math.pow(lat1, 2)
                - 44.54 * Math.pow(lon1, 3);
        return e;
    }

    /**
     *
     * @param lon longitude
     * @param lat latitude
     * @return retourne la coordonnée N (nord) du point de longitude lon et latitude lat dans le système WGS84.
     */
    public static double n(double lon, double lat){
        lon = Math.toDegrees(lon);
        lat = Math.toDegrees(lat);
        double lon1 = Math.pow(10, -4) * (3600 * lon - 26782.5);
        double lat1 = Math.pow(10, -4) * (3600 * lat - 169028.66);
        double n = 1200147.07
                + 308807.95 * lat1
                + 3745.25 * Math.pow(lon1, 2)
                + 76.63 * Math.pow(lat1, 2)
                - 194.56 * Math.pow(lon1, 2) * lat1
                + 119.79 * Math.pow(lat1, 3);
        return n;
    }

    /**
     *
     * @param e cordonnée est
     * @param n coordonnée nort
     * @return retourne la longitude dans le système WGS84.
     */
    public static double lon(double e, double n){

        double x = Math.pow(10, -6) * (e - 2600000);
        double y = Math.pow(10, -6) * (n - 1200000);
        double lon0 = 2.6779094
                + 4.728982*x
                + 0.791484*x*y
                + 0.1306*x*Math.pow(y,2)
                - 0.0436*Math.pow(x,3);

        return Math.toRadians(lon0 * 100 / 36);

    }

    /**
     *
     * @param e coordonnée est
     * @param n coordonnée nord
     * @return  retourne la latitude dans le système WGS84.
     */
    public static double lat(double e, double n){

        double x = Math.pow(10, -6) * (e - 2600000);
        double y = Math.pow(10, -6) * (n - 1200000);
        double lat0 = 16.9023892
                + 3.238272*y
                - 0.270978*Math.pow(x,2)
                - 0.002528*Math.pow(y,2)
                - 0.0447*Math.pow(x,2)*y
                - 0.0140*Math.pow(y,3);
        return Math.toRadians(lat0 * 100 / 36) ;
    }
}
