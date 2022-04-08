package ch.epfl.javelo.projection;

import ch.epfl.javelo.Math2;


/**
 * Un point sur la terre caractérisé par ses coordonnées
 * dans le système de coordonnées Mercator.
 *
 * @param x La coordonnée x du point dans le système Mercator.
 * @param y La coordonnée y di point dans le système Mercator.
 * @author Yoan Giovannini (303934)
 */
public record PointWebMercator(double x, double y) {

    /**
     * Constructeur compacte, vérifie que le point est
     * dans le système Mercator (entre 0 et 1).
     *
     * @param x La coordonnée x du point.
     * @param y La coordonnée y du point.
     * @throws IllegalArgumentException si x ou y n'est pas dans [0,1]
     */
    public PointWebMercator {
        if (x > 1 || x < 0 || y > 1 || y < 0)
            throw new IllegalArgumentException("x ou y est hors de [0,1]");
    }

    /**
     * Calcule les coordonnées depuis un point dans le système Mercator agrandis vers un
     * point non agrandis.
     *
     * @param zoomLevel le niveau d'agrandissement de la carte dans le système Mercator.
     * @param x une coordonnée x dans le système Mercator.
     * @param y une coordonnée y dans le système Mercator.
     * @return un point (x,y) dans le système Mercator avec un agrandissement nul.
     */
    public static PointWebMercator of(int zoomLevel, double x, double y) {
        double x2 = Math.scalb(x, -(8 + zoomLevel));
        double y2 = Math.scalb(y, -(8 + zoomLevel));
        return new PointWebMercator(x2, y2);
    }

    /**
     * Calcule les coordonnées d'un point Mercator depuis le système suisse.
     *
     * @param pointCh un point dans le système suisse.
     * @return un point dans le système Mercator.
     */
    public static PointWebMercator ofPointCh(PointCh pointCh) {
        double x = (pointCh.lon() + Math.PI) / (2 * Math.PI);
        double y = (Math.PI - Math2.asinh(Math.tan(pointCh.lat()))) / (2 * Math.PI);
        return new PointWebMercator(x, y);
    }

    /**
     * Calcule la coordonnée x au niveau d'agrandissement donné
     * en paramètre.
     *
     * @param zoomLevel le niveau d'agrandissement.
     * @return la coordonnée x au niveau d'agrandissement.
     */
    public double xAtZoomLevel(int zoomLevel) {
        return Math.scalb(x, 8 + zoomLevel);
    }

    /**
     * Calcule la coordonnée y au niveau d'agrandissement donné
     * en paramètre.
     *
     * @param zoomLevel le niveau d'agrandissement.
     * @return la coordonnée y au niveau d'agrandissement.
     */
    public double yAtZoomLevel(int zoomLevel) {
        return Math.scalb(y, 8 + zoomLevel);
    }

    /**
     * Calcule la longitude du point Mercator.
     *
     * @return la longitude du point.
     */
    public double lon() {
        return 2 * Math.PI * x - Math.PI;
    }

    /**
     * Calcule la latitude du point Mercator.
     *
     * @return la latitude du point.
     */
    public double lat() {
        return Math.atan(Math.sinh(Math.PI - 2 * Math.PI * y));
    }

    /**
     * Calcule les coordonnées du point Mercator dans le système suisse.
     *
     * @return un point dans le système suisse.
     */
    public PointCh toPointCh() {
        double lon = this.lon();
        double lat = this.lat();
        double e = Ch1903.e(lon, lat);
        double n = Ch1903.n(lon, lat);
        return SwissBounds.containsEN(e, n) ? new PointCh(e, n) : null;
    }
}
