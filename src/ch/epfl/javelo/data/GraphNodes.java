package ch.epfl.javelo.data;

import ch.epfl.javelo.Preconditions;

import java.nio.IntBuffer;

/**
 *
 * @author Harrishan Raveendran (345291)
 */

public record GraphNodes(IntBuffer buffer) {

    private static final int OFFSET_E = 0;
    private static final int OFFSET_N = OFFSET_E + 1;
    private static final int OFFSET_OUT_EDGES = OFFSET_N + 1;
    private static final int NODE_INTS = OFFSET_OUT_EDGES + 1;

    /**
     *
     * @return le nombre total de noeuds.
     */
    public int count(){
        return (int) (buffer.capacity() / 3);
    }

    /**
     *
     * @param nodeId noeud identité
     * @return la coordonée E du noeud identité.
     */
    public double nodeE(int nodeId){
        return buffer.get(nodeId + OFFSET_E);
    }

    /**
     *
     * @param nodeId noeud identité
     * @return la coordonnée N du noeud identité
     */
    public double nodeN(int nodeId){
        return buffer.get(nodeId + OFFSET_N);
    }

    /**
     *
     * @param nodeId noeud identité
     * @return le nombre d'arrêts sortant du noeud identité
     */
    public int outDegree(int nodeId){
        int nb = buffer.get(nodeId + OFFSET_OUT_EDGES);
        return nb >>> 28;
    }

    /**
     *
     * @param nodeId noeud identité
     * @param edgeIndex index
     * @return l'identité de l'arrête à l'index donné.
     */
    public int edgeId(int nodeId, int edgeIndex){
        Preconditions.checkArgument(0 <= edgeIndex && edgeIndex < outDegree(nodeId));
        int nb = buffer.get(nodeId + OFFSET_OUT_EDGES);
        return nb << 4;
    }

}
