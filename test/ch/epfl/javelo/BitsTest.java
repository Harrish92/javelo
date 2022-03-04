package ch.epfl.javelo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BitsTest {

    // res 1010111111101011101010111110000
    // 01|01010111111101011101010111110000


    @Test
    void NegLengthTest(){
        assertThrows(IllegalArgumentException.class, ()->{
            var b = Bits.extractUnsigned(0b11001010111111101011101010111110, -1, 4);
        });
    }

    @Test
     void UnsignedLength32(){
        assertThrows(IllegalArgumentException.class, ()->{
            var b = Bits.extractUnsigned(0b11001010111111101011101010111110, -1, 32);
        });
    }

    @Test
    void extractSignedTest(){
        var b = Bits.extractSigned(0b11001010111111101011101010111110,8,4); // value en binaire :
        assertEquals(-6, b);
    }

    @Test
    void extractUnSignedTest(){
        var b = Bits.extractUnsigned(0b11001010111111101011101010111110,8,4);
        assertEquals(10, b);
    }

}
