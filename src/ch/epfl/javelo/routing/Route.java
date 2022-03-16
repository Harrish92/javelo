package ch.epfl.javelo.routing;

import ch.epfl.javelo.projection.PointCh;

import java.util.List;

/**
 * Représente un itinéraire.
 * @author Yoan Giovannini (303934)
 */
public abstract interface Route {

    //Retourne l'index du segment à la position donnée (en mètres);
    public abstract int indexOfSegmentAt(double position);

    //Retourne la longueur de l'itinéraire, en mètres.
    public abstract double length();

    //Retourne la totalité des arêtes de l'itinéraire.
    public abstract List<Edge> edges();

    //Retourne la totalité des points situés aux extrémités des arêtes de l'itinéraire.
    public abstract List<PointCh> points();

    //Retourne le point se trouvant à la position donnée le long de l'itinéraire.
    public abstract PointCh pointAt(double position);

    //Retourne l'altitude à la position donnée le long de l'itinéraire.
    public abstract double elevationAt(double position);

    //Retourne l'identité du nœud appartenant à l'itinéraire et se trouvant le plus proche de la position donnée.
    public abstract int nodeClosestTo(double position);

    //Retourne le point de l'itinéraire se trouvant le plus proche du point de référence donné.
    public abstract RoutePoint pointClosestTo(PointCh point);
}
