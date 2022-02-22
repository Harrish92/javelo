package ch.epfl.javelo.projection;

import ch.epfl.javelo.Preconditions;

/**
 * Un point en suisse caractérisé par ses coordonnées.
 *
 * @author Yoan Giovannini (303934)
 *
 * @param e La coordonnée E.
 * @param n La coordonnée N.
 */
public record PointCh(double e, double n) {

    public PointCh {
        Preconditions.checkArgument(SwissBounds.containsEN(e, n));
    }

    public double squaredDistanceTo(PointCh that) {
        double deltaE = Math.abs(this.e - that.e());
        double deltaN = Math.abs(this.n - that.n());
        return Math.hypot(deltaE, deltaN);
    }

    public double distanceTo(PointCh that) {
        return Math.pow(this.squaredDistanceTo(that), 1/2);
    }

    public double lon() {

    }

    public double lat() {

    }

}
