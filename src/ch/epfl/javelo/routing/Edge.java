package ch.epfl.javelo.routing;

import ch.epfl.javelo.data.Graph;
import ch.epfl.javelo.projection.PointCh;

import java.util.function.DoubleUnaryOperator;

/**
 *
 * @author Harrishan Raveendran (345291)
 *
 * @param fromNodId l'identité du noeud de départ de l'arête
 * @param toNodeId l'identité du noeud d'arrivée de l'arête
 * @param fromPoint le point de départ de l'arête
 * @param toPoint le point d'arrivée de l'arête
 * @param length la longueur de l'arête en mètre
 * @param profile le profil en long de l'arête
 */

public record Edge(int fromNodId, int toNodeId, PointCh fromPoint, PointCh toPoint, double length,
                   DoubleUnaryOperator profile) {

    /**
     *
     * @param graph graphe
     * @param edgeId l'identité de l'arête
     * @param fromNodeId l'identité du noeud de départ de l'arête
     * @param toNodeId l'identité du noeud d'arrivée de l'arête
     * @return une instance de edge avec les attributs donnés.
     */
    public static Edge of(Graph graph,int edgeId, int fromNodeId, int toNodeId){
        return null; //Edge(fromNodeId, toNodeId, graph);
    }

    /**
     *
     * @param point point coordonnée
     * @return la position qui se trouve au long de l'arête, en mètre, le plus proche de
     */
    public double positionClosestTo(PointCh point){
        return 0.0;
    }

    /**
     *
     * @param position une position
     * @return le point se trouvant à la position donnée sur l'arête.
     */
    public PointCh pointAt(double position){
        return null;
    }

    /**
     *
     * @param position une position
     * @return l'altitude, en mètre, de la position donnée sur l'arrête.
     */
    public double elevationAt(double position){
        return 0.0;
    }
}
