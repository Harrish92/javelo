package ch.epfl.javelo.routing;

import ch.epfl.javelo.Functions;
import ch.epfl.javelo.Math2;
import ch.epfl.javelo.data.Graph;
import ch.epfl.javelo.projection.PointCh;

import java.util.function.DoubleUnaryOperator;

/**
 *
 * @author Harrishan Raveendran (345291)
 *
 * @param fromNodeId l'identité du noeud de départ de l'arête
 * @param toNodeId l'identité du noeud d'arrivée de l'arête
 * @param fromPoint le point de départ de l'arête
 * @param toPoint le point d'arrivée de l'arête
 * @param length la longueur de l'arête en mètre
 * @param profile le profil en long de l'arête
 */

public record Edge(int fromNodeId, int toNodeId, PointCh fromPoint, PointCh toPoint, double length,
                   DoubleUnaryOperator profile) {

    /**
     *
     * @param graph graphe
     *
     * @param edgeId l'identité de l'arête
     * @param fromNodeId l'identité du noeud de départ de l'arête
     * @param toNodeId l'identité du noeud d'arrivée de l'arête
     * @return une instance de edge avec les attributs donnés.
     */
    public static Edge of(Graph graph,int edgeId, int fromNodeId, int toNodeId){
        return new Edge(fromNodeId, toNodeId, graph.nodePoint(fromNodeId), graph.nodePoint(toNodeId),
                graph.edgeLength(edgeId) , graph.edgeProfile(edgeId));
    }

    /**
     *
     * @param point point coordonnée
     * @return la position qui se trouve sur le  long de l'arête, en mètre, la plus proche du point.
     */
    public double positionClosestTo(PointCh point){
        return Math2.projectionLength(fromPoint.e(), fromPoint.n(), toPoint.e(), toPoint.n(), point.e(), point.n());
    }

    /**
     *
     * @param position une position
     * @return le point se trouvant à la position donnée sur l'arête.
     */
    public PointCh pointAt(double position){
        double pourcentage = position / length;
        return new PointCh(Math2.interpolate(fromPoint.e(), toPoint.e(), pourcentage),
                Math2.interpolate(fromPoint.n(), toPoint.n(), pourcentage));
    }

    /**
     *
     * @param position une position
     * @return l'altitude, en mètre, de la position donnée sur l'arrête.
     */
    public double elevationAt(double position){
        return profile.applyAsDouble(position);
    }
}
