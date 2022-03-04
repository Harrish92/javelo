package ch.epfl.javelo.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AttributeSetTest {

    @Test
    void erreurTest() {
        long finalBits = 1L << Attribute.COUNT;
        assertThrows(IllegalArgumentException.class, ()
                -> new AttributeSet(finalBits));
        assertDoesNotThrow(() -> new AttributeSet(1L << Attribute.COUNT - 1));
    }

    @Test
    void valuesTest() {
        long bits = 0L;
        AttributeSet att0 = new AttributeSet(bits);
        for(int i = 0; i <Attribute.COUNT; i++) {
            assertFalse(att0.contains(Attribute.values()[i]));
        }
        for(int i = 0; i < Attribute.COUNT; i++) {
            bits += 1L << i;
        }
        AttributeSet att = new AttributeSet(bits);
        for(int i = 0; i <Attribute.COUNT; i++) {
            assertTrue(att.contains(Attribute.values()[i]));
            System.out.println(i);
        }
        bits = bits >> 2;
        AttributeSet att2 = new AttributeSet(bits);
        assertFalse(att2.contains(Attribute.values()[Attribute.COUNT-1]));
        assertFalse(att2.contains(Attribute.values()[Attribute.COUNT-2]));
        assertTrue(att2.contains(Attribute.values()[Attribute.COUNT-3]));
        assertTrue(att2.bits() == bits);
        assertFalse(att2.intersects(new AttributeSet(1L << 3)));
        assertTrue(att2.intersects(new AttributeSet(bits)));
    }

}