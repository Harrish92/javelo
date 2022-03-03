package ch.epfl.javelo.data;

import ch.epfl.javelo.Preconditions;

import java.util.StringJoiner;

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
        /*int x = 0;
        long max = 0L;
        while(x < Attribute.COUNT){
            max += Math.pow(2, x);
            x++;
        }
        Preconditions.checkArgument(bits <= max);*/
        Preconditions.checkArgument(bits < 1L << Attribute.COUNT );

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
        return (attribute.ordinal() >> bits) % 2 == 1;

    }

    /**
     * Vérifie si cette liste (this) et la liste d'attributs
     * passée en argument correspondes.
     * @param that une autre liste d'attributs
     * @return vrai si les deux listes correspondent, faux autrement.
     */
    public boolean intersects(AttributeSet that) {
        return bits == that.bits;
    }

    @Override
    public String toString() {
        StringJoiner str = new StringJoiner(", ", "{", "}");
        for(int i = 0; i < Attribute.COUNT; i++) {
            if(i >> bits % 2 == 1){
                str.add(Attribute.values()[i].keyValue());
            }
        }
        return str.toString();
    }

}
