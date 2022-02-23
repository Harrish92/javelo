package ch.epfl.javelo.projection;

import ch.epfl.javelo.Preconditions;

/**
 * Un point en suisse caractérisé par ses coordonnées dans
 * le système suisse.
 *
 * @author Yoan Giovannini (303934)
 *
 * @param e La coordonnée E (est).
 * @param n La coordonnée N (nord).
 */
public record PointCh(double e, double n) {

    /**
     *Constructeur compacte, vérifie que le point est en suisse.
     *
     * @param e La coordonnée E du point.
     * @param n La coordonnée N du point.
     */
    public PointCh {
        Preconditions.checkArgument(SwissBounds.containsEN(e, n));
    }

    /**
     *Calcule la distance au carré entre deux points.
     *
     * @param that Un autre point.
     * @return La distance au carré.
     */
    public double squaredDistanceTo(PointCh that) {
        return Math.pow(this.distanceTo(that), 2);
    }

    /**
     *Calcule la distance entre ce point et un autre point.
     *
     * @param that Un autre point.
     * @return La distance.
     */
    public double distanceTo(PointCh that) {
        double deltaE = Math.abs(this.e - that.e());
        double deltaN = Math.abs(this.n - that.n());
        return Math.hypot(deltaE, deltaN);
    }

    /**
     * Calcule la longitude du point dans le système WGS84.
     * @return La longitude en radians.
     */
    public double lon() {
        return Ch1903.lon(e, n);
    }

    /**
     * Calcule la latitude du point dans le système WGS84.
     * @return La latitude en radians.
     */
    public double lat() {
        return Ch1903.lat(e, n);
    }

}
