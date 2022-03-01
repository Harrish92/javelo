package ch.epfl.javelo.data;

/**
 *
 * @author Yoan Giovannini (303934)
 */
public record AttributeSet(long bits) {

    /**
     * constructeur compact
     * @param bits
     */
    public AttributeSet {

    }

    public static AttributeSet of(Attribute... attributes) {
        return null;
    }

    public boolean contains(Attribute attribute) {
        return false;

    }

    public boolean intersects(AttributeSet that) {
        return false;
    }

    @Override
    public String toString() {
        return "";
    }

}
