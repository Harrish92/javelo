package ch.epfl.javelo.data;

import ch.epfl.javelo.Preconditions;

/**
 * Représente les attributs sous la forme d'un nombre binaire
 *
 * @author Yoan Giovannini (303934)
 */
public record AttributeSet(long bits) {

    /**
     * constructeur compact
     * @param bits
     */
    public AttributeSet {
        int x = 0;
        long max = 0L;
        while(x < Attribute.COUNT){
            max += Math.pow(2, x);
            x++;
        }
        Preconditions.checkArgument(bits <= max);
    }

    /**
     * Créé un nouvel AttributeSet à partir d'une liste d'attributs.
     * @param attributes une liste d'attributs.
     * @return un objet représentant un ensemble d'attributs.
     */
    public static AttributeSet of(Attribute... attributes) {
        long attributeSet = 0L;
        for(Attribute attribute : attributes) {
            if((attribute.ordinal() >> attributeSet) % 2 == 0) {
                attributeSet += 1L << attribute.ordinal();
                //attributeSet+= Math.pow(2, attribute.ordinal());
            }
        }
        return new AttributeSet(attributeSet);
    }

    /**
     * Vérifie que cette liste (this) contient l'attribut donné en argument.
     * @param attribute un attribut.
     * @return vrai si la liste contient l'attribut, faux autrement.
     */
    public boolean contains(Attribute attribute) {
        return false;

    }

    /**
     *
     * @param that
     * @return
     */
    public boolean intersects(AttributeSet that) {
        return false;
    }

    @Override
    public String toString() {
        return "";
    }

}
