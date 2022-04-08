package ch.epfl.javelo.projection;

/**
 * Définit les coordonnées limites de la suisse
 * et possède une méthode qui vérifie des coordonnées.
 *
 * @author Yoan Giovannini (303934)
 */
public final class SwissBounds {

    //
    public final static double MIN_E = 2485000;
    public final static double MAX_E = 2834000;
    public final static double MIN_N = 1075000;
    public final static double MAX_N = 1296000;
    // longueur et largeur de la Suisse
    public final static double WIDTH = MAX_E - MIN_E;
    public final static double HEIGHT = MAX_N - MIN_N;

    /**
     * classe non instantiable
     */
    private SwissBounds(){}

    /**
     * Vérifie si les coordonnées données en paramètre sont en suisse.
     *
     * @param e La coordonnée E.
     * @param n La coordonnée N.
     * @return Un booléen vrai si les coordonnées données en paramètre
     * sont en suisse et faux autrement.
     */
    public static boolean containsEN(double e, double n) {
        return e <= MAX_E && e >= MIN_E && n <= MAX_N && n >= MIN_N;
    }
}
