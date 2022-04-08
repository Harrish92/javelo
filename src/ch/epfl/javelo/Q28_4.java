package ch.epfl.javelo;

/**
 *
 * @author Harrishan Raveendran (345291)
 *
 * Q28_4 permet la conversion des nombres entre la représentation Q28.4 et d'autres représentation
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
     * @return un valeure entière dans le format Q.28
     */
    public static int ofInt(int i){
        return i << 4;

    }

    /**
     *
     * @param q28_4 un entier
     * @return le changement de base de Q28_4 à un double.
     */
    public static double asDouble(int q28_4){
        return Math.scalb((double) q28_4, -4);
    }

    /**
     *
     * @param q28_4 un entier
     * @return le changement de base de Q28_4 à un float.
     */
    public static float asFloat(int q28_4){
        return Math.scalb(q28_4, -4);

    }
}
