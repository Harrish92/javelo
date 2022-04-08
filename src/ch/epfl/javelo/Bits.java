package ch.epfl.javelo;

/**
 * @author Harrishan Raveendran (345291)$
 * <p>
 * Bits permet l'extraction d'une séquence de bits d'un vecteur de 32 bits
 */
public final class Bits {

    /**
     * classe non instanciable
     */
    private Bits() {
    }

    /**
     * @param value  un nombre entier
     * @param start  index
     * @param length longueur
     * @return un entier qui provient d'un extrait de bits interprêté comme signé.
     */
    public static int extractSigned(int value, int start, int length) {
        Preconditions.checkArgument((start >= 0) && (length >= 0) && (length <= 32) && (start <= 32)
                && (start + length) <= 32);
        int nb = value << Integer.SIZE - (start + length);

        return nb >> Integer.SIZE - length;

    }

    /**
     * @param value  un nombre entier
     * @param start  index
     * @param length longueur
     * @return un entier qui provient d'un extrait de bits interprêté comme non-signé.
     */
    public static int extractUnsigned(int value, int start, int length) {
        Preconditions.checkArgument((start >= 0) && (length >= 0) && (length <= 31) && (start <= 31)
                && ((start + length) <= 31));
        int nb = value << Integer.SIZE - (start + length);

        return nb >>> Integer.SIZE - length;


    }


}

