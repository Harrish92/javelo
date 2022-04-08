package ch.epfl.javelo.projection;

/**
 *
 *
 * @author Harrishan Raveendran (345291)
 *
 * permet la conversion entre les coordonnées WSG 84 et les coordonnées suisses
 */
public final class Ch1903 {

    /**
     *
     * @param lon longitude
     * @param lat latitude
     * @return la coordonnée E (est) avec les points de longitude lon et latitude lat.
     */
    public static double e(double lon, double lat){
        double lambda = Math.pow(10, -4) * (3600 * Math.toDegrees(lon) - 26782.5);
        double phi = Math.pow(10, -4) * (3600 * Math.toDegrees(lat) - 169028.66);
        return 2600072.37
                + 211455.93 * lambda
                - 10938.51 * lambda * phi
                - 0.36 * lambda * Math.pow(phi, 2)
                - 44.54 * Math.pow(lambda, 3);
    }

    /**
     *
     * @param lon longitude
     * @param lat latitude
     * @return la coordonnée N (nord) avec les points de longitude lon et latitude lat.
     */
    public static double n(double lon, double lat){
        double lambda = Math.pow(10, -4) * (3600 * Math.toDegrees(lon) - 26782.5);
        double phi = Math.pow(10, -4) * (3600 * Math.toDegrees(lat) - 169028.66);
        return 1200147.07
                + 308807.95 * phi
                + 3745.25 * Math.pow(lambda, 2)
                + 76.63 * Math.pow(phi, 2)
                - 194.56 * Math.pow(lambda, 2) * phi
                + 119.79 * Math.pow(phi, 3);
    }

    /**
     *
     * @param e cordonnée est
     * @param n coordonnée nort
     * @return la longitude.
     */
    public static double lon(double e, double n){

        double x = Math.pow(10, -6) * (e - 2600000);
        double y = Math.pow(10, -6) * (n - 1200000);
        double lon0 = 2.6779094
                + 4.728982 * x
                + 0.791484 * x * y
                + 0.1306 * x * Math.pow(y,2)
                - 0.0436 * Math.pow(x,3);

        return Math.toRadians(lon0 * 100 / 36);

    }

    /**
     *
     * @param e coordonnée est
     * @param n coordonnée nord
     * @return  la latitude.
     */
    public static double lat(double e, double n){

        double x = Math.pow(10, -6) * (e - 2600000);
        double y = Math.pow(10, -6) * (n - 1200000);
        double lat0 = 16.9023892
                + 3.238272 * y
                - 0.270978 * Math.pow(x,2)
                - 0.002528 * Math.pow(y,2)
                - 0.0447 * Math.pow(x,2) * y
                - 0.0140 * Math.pow(y,3);

        return Math.toRadians(lat0 * 100 / 36) ;
    }
}
