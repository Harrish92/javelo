package ch.epfl.javelo.data;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public record GraphEdges(ByteBuffer edgesBuffer, IntBuffer profileIds, ShortBuffer elevations) {

    /**
     *
     * @param edgeId arrête identité
     * @return vrai ssi l'arrête
     */
/*    public boolean isInverted(int edgeId){

    }

    public int targetNodeId(int edgeId){

    }

    public double length(int edgeId){

    }

    public double elevationGain(int edgeId){

    }

    public boolean hasProfile(int edgeId){

    }

    public float[] profileSamples(int edgeId){

    }

    public int attributesIndex(int edgeId){

    }

*/
}
