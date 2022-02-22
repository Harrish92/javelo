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

    }

    public double distanceTo(PointCh that) {

    }

}
