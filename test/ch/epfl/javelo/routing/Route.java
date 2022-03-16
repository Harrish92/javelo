package ch.epfl.javelo.routing;

import ch.epfl.javelo.projection.PointCh;

import java.util.List;

/**
 * @author Harrishan Raveendran (345291)
 */
public interface Route {

    /**
     *
     * @param position une position
     * @return retourne l'index du segment à la position donnée (en mètres)
     */
    int indexOfSegmentAt(double position);

    /**
     *
     * @return la longueur de l'itinéraire, en mètres
     */
    double length();

    /**
     *
     * @return la totalité des arêtes de l'itinéraire
     */
    List<Edge> edges();

    /**
     *
     * @return la totalité des points situés aux extrémités des arêtes de l'itinéraire
     */
    List<PointCh> points();

    /**
     *
     * @param position une position
     * @return retourne le point se trouvant à la position donnée le long de l'itinéraire
     */
    PointCh pointAt(double position);

    /**
     *
     * @param position une position
     * @return l'altitude à la position donnée le long de l'itinéraire
     */
    double elevationAt(double position);

    /**
     *
     * @param position une position
     * @return l'identité du nœud appartenant à l'itinéraire et se trouvant le plus proche de la position donnée
     */
    int nodeClosestTo(double position);

    /**
     *
     * @param point un point
     * @return le point de l'itinéraire se trouvant le plus proche du point de référence donné
     */
    RoutePoint pointClosestTo(PointCh point);


}
