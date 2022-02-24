package ch.epfl.javelo;

/**
 * Préconditions à l'exécution d'un algorithme.
 *
 * @author Yoan Giovannini (303934)
 */
public final class Preconditions {

    /**
     * Classe non instantiable
     */
    private Preconditions() {}

    /**
     * Lève une exception de type IllegalArgumentException
     * si le paramètre shouldBeTrue est faux.
     *
     * @param shouldBeTrue Le paramètre.
     * @throws IllegalArgumentException si le paramètre est faux.
     */
    public static void checkArgument(boolean shouldBeTrue) {
        if(!shouldBeTrue){
            throw new IllegalArgumentException();
        }
    }
}
