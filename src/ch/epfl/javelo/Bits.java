package ch.epfl.javelo;

/**
 *
 * @author Harrishan Raveendran (345291)
 */
public final class Bits {

    /**
     * classe non instanciable
     */
    private Bits(){

    }

    /**
     *
     * @param value un nombre entier
     * @param start index
     * @param length longueur
     * @return un entier qui provient d'un extrait de bits interprêté comme signé.
     */
    public static int extractSigned(int value, int start, int length){
        Preconditions.checkArgument((start >= 0) && (length >= 0) && (length <= 32) && (start <= 32));
        Preconditions.checkArgument((start + length) <= 32);
        int bit32 = value << Integer.SIZE-(start+length);
        bit32 = bit32 >> Integer.SIZE - length;
        return bit32;

    }

    /**
     *
     * @param value un nombre entier
     * @param start index
     * @param length longueur
     * @return un entier qui provient d'un extrait de bits interprêté comme non-signé.
     */
    public static int extractUnsigned(int value, int start, int length){
        Preconditions.checkArgument((start >= 0) && (length >= 0) && (length <= 31) && (start <= 31));
        Preconditions.checkArgument((start + length) <= 31);
        int bit31 = value << Integer.SIZE-(start+length);
        bit31 = bit31 >>> Integer.SIZE - length;

        return bit31;


    }


}

