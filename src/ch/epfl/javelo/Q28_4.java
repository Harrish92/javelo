package ch.epfl.javelo;

/**
 *
 * @author Harrishan Raveendran (345291)
 */

public final class Q28_4 {

    /**
     * classe non instanciable
     */
    private Q28_4(){

    }

    /**
     *
     * @param i nombre entier
     * @return conversion d'un entier dans le format Q.28
     */
    public static int ofInt(int i){
        return (int) Math.scalb(i, 4);

    }

    /**
     *
     * @param q28_4 un entier
     * @return une valeur en double dans le format Q.28
     */
    public static double asDouble(int q28_4){
        return Math.scalb(q28_4, -4);
    }

    /**
     *
     * @param q28_4 un entier
     * @return une valeur en float dans le format Q.28
     */
    public static float asFloat(int q28_4){
        return Math.scalb(q28_4, -4);

    }
}
