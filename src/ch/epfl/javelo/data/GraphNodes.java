package ch.epfl.javelo.data;

import ch.epfl.javelo.Bits;
import ch.epfl.javelo.Q28_4;

import java.nio.IntBuffer;

/**
 * GraphNodes représente le tableau de tous les nœuds du graphe.
 *
 * @param buffer mémoire tampon qui contient la valeur des attributs de tous les nœuds du graphe
 * @author Harrishan Raveendran (345291)
 */
public record GraphNodes(IntBuffer buffer) {

    // décalages qui permet de retrouver l'attribut souhaité dans le buffer
    private static final int OFFSET_E = 0;
    private static final int OFFSET_N = OFFSET_E + 1;
    private static final int OFFSET_OUT_EDGES = OFFSET_N + 1;
    private static final int NODE_INTS = OFFSET_OUT_EDGES + 1;

    /**
     * @return le nombre total de noeuds.
     */
    public int count() {
        return (buffer.capacity() / NODE_INTS);
    }

    /**
     * @param nodeId noeud identité
     * @return la coordonée E du noeud identité.
     */
    public double nodeE(int nodeId) {
        return Q28_4.asDouble(buffer.get(nodeId * NODE_INTS + OFFSET_E));
    }

    /**
     * @param nodeId noeud identité
     * @return la coordonnée N du noeud identité
     */
    public double nodeN(int nodeId) {
        return Q28_4.asDouble(buffer.get(nodeId * NODE_INTS + OFFSET_N));
    }

    /**
     * @param nodeId noeud identité
     * @return le nombre d'arrêts sortant du noeud identité
     */
    public int outDegree(int nodeId) {
        return buffer.get(nodeId * NODE_INTS + OFFSET_OUT_EDGES) >>> 28;
    }

    /**
     * @param nodeId noeud identité
     * @param edgeIndex index
     * @return l'identité de l'arrête à l'index donné.
     */
    public int edgeId(int nodeId, int edgeIndex) {
        return Bits.extractUnsigned(buffer.get(nodeId * NODE_INTS + OFFSET_OUT_EDGES), 0, 28) + edgeIndex;
    }

}
