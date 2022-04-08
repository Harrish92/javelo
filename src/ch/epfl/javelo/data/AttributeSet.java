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
     * @param bits représente une séquence de bits
     */
    public AttributeSet {
        Preconditions.checkArgument(bits >> Attribute.COUNT == 0);
    }

    /**
     * Créé un nouvel AttributeSet à partir d'une liste d'attributs.
     * @param attributes une liste d'attributs.
     * @return un objet représentant un ensemble d'attributs.
     */
    public static AttributeSet of(Attribute... attributes) {
        long attributeSet = 0L;
        for(Attribute attribute : attributes) {
            if((attributeSet >> attribute.ordinal()) % 2 == 0)
                attributeSet += 1L << attribute.ordinal();
        }
        return new AttributeSet(attributeSet);
    }

    /**
     * Vérifie que cette liste (this) contient l'attribut donné en argument.
     * @param attribute un attribut.
     * @return vrai si la liste contient l'attribut, faux autrement.
     */
    public boolean contains(Attribute attribute) {
        return (bits >> attribute.ordinal()) % 2 == 1;

    }

    /**
     * Vérifie si cette liste (this) et la liste d'attributs
     * passée en argument correspondes.
     * @param that une autre liste d'attributs
     * @return vrai si les deux listes correspondent, faux autrement.
     */
    public boolean intersects(AttributeSet that) {
        for(int i = 0; i < Attribute.COUNT; i++) {
            if((bits >> i) % 2 == 1 && (that.bits() >> i) % 2 == 1){
                return true;
            }
        }
        return false;
    }

    //Renvoie la liste des attributs sous forme de chaine de caractères.
    @Override
    public String toString() {
        StringJoiner str = new StringJoiner(",", "{", "}");
        for(int i = 0; i < Attribute.COUNT; i++) {
            if((bits >> i) % 2 == 1){
                str.add(Attribute.values()[i].keyValue());
            }
        }
        return str.toString();
    }

}
