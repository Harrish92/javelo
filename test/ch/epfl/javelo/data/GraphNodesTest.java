package ch.epfl.javelo.data;

import org.junit.jupiter.api.Test;

import java.nio.IntBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GraphNodesTest {

    @Test
    public void countTest(){
        IntBuffer b = IntBuffer.wrap(new int[]{
                2_600_000 << 4,
                1_200_000 << 4,
                0x2_000_1234
        });
        GraphNodes GN = new GraphNodes(b);
        int expected = 1;
        int actual = GN.count();
        assertEquals(expected, actual);

    }

    @Test
    public void nodeETest(){
        IntBuffer b = IntBuffer.wrap(new int[]{
                2_600_000 << 4,
                1_200_000 << 4,
                0x2_000_1234
        });
        GraphNodes GN = new GraphNodes(b);
        double expected = 2_600_000 << 4;
        double actual = GN.nodeE(0);
        assertEquals(expected, actual);
    }

    @Test
    public void nodNTest(){
        IntBuffer b = IntBuffer.wrap(new int[]{
                2_600_000 << 4,
                1_200_000 << 4,
                0x2_000_1234
        });
        GraphNodes GN = new GraphNodes(b);
        double expected = 1_200_000 << 4;
        double actual = GN.nodeN(0);
        assertEquals(expected, actual);
    }

    @Test
    public void outDegreeTest(){
        IntBuffer b = IntBuffer.wrap(new int[]{
                2_600_000 << 4,
                1_200_000 << 4,
                0x2_000_1234
        });
        GraphNodes GN = new GraphNodes(b);
        int expected = 2;
        int actual = GN.outDegree(0);
        assertEquals(expected, actual);
    }

    @Test
    public void edgeIdTest0(){
        IntBuffer b = IntBuffer.wrap(new int[]{
                2_600_000 << 4,
                1_200_000 << 4,
                0x2_000_1234
        });
        GraphNodes GN = new GraphNodes(b);
        int expected = 4660;
        int actual  =GN.edgeId(0, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void edgeIdTest1(){
        IntBuffer b = IntBuffer.wrap(new int[]{
                2_600_000 << 4,
                1_200_000 << 4,
                0x2_000_1234
        });
        GraphNodes GN = new GraphNodes(b);
        int expected = 4661;
        int actual  =GN.edgeId(0, 1);
        assertEquals(expected, actual);
    }
}
