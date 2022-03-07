package ch.epfl.javelo.data;

import java.nio.file.Path;
import java.util.List;

/**
 * La classe Graph représente de graphe de javelo.
 */
public final class Graph {

    /**
     * Constructeur de la classe Graph.
     * @param nodes
     * @param sectors
     * @param edges
     * @param attributeSets
     */
    public Graph(GraphNodes nodes, GraphSectors sectors, GraphEdges edges,
                 List<AttributeSet> attributeSets) {

    }

    public static Graph loadFrom(Path basePath) {
        return null;
    }
}
