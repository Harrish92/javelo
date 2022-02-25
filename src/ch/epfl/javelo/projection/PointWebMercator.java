package ch.epfl.javelo.projection;

import ch.epfl.javelo.Preconditions;

/**
 * Un point sur la terre caractérisé par ses coordonnées
 * dans le système de coordonnées Mercator.
 *
 * @author Yoan Giovannini (303934)
 *
 * @param x La coordonnée x du point dans le système Mercator.
 * @param y La coordonnée y di point dans le système Mercator.
 */
public record PointWebMercator(double x, double y) {

    /**
     *Constructeur compacte, vérifie que le point est
     * dans le système Mercator (entre 0 et 1).
     *
     * @param x La coordonnée x du point.
     * @param y La coordonnée y du point.
     */
    public PointWebMercator {
        if (x > 1 || x < 0 || y > 1 || y < 0){

        }
    }
}
