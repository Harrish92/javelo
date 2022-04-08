package ch.epfl.javelo.routing;

import ch.epfl.javelo.Math2;
import ch.epfl.javelo.Preconditions;
import ch.epfl.javelo.projection.PointCh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * représente un itinéraire simple
 *
 * @author Harrishan Raveendran (345291)
 */

public final class SingleRoute implements Route {
    private final List<Edge> edges;
    private final double[] tab;

    /**
     * @param edges liste d'edge
     *              Constructeur public qui retourne l'itinéraire simple composé des arêtes données,
     *              ou lève IllegalArgumentException si la liste d'arêtes est vide.
     */
    public SingleRoute(List<Edge> edges) {
        Preconditions.checkArgument(!edges.isEmpty());
        this.edges = List.copyOf(edges);


        tab = new double[edges.size() + 1];
        double longueur = 0;
        tab[0] = longueur;

        for (int i = 0; i < this.edges.size(); i++) {
            longueur += this.edges.get(i).length();
            tab[i + 1] = longueur;
        }

    }

    /**
     * @param position position de l'itinéraire simple
     * @return l'index du segment de l'itinéraire contenant la position donnée,
     * qui vaut toujours 0 dans le cas d'un itinéraire simple
     */
    @Override
    public int indexOfSegmentAt(double position) {
        return 0;
    }

    /**
     * @return la longueur de l'itinéraire, en mètres
     */
    @Override
    public double length() {
        double length = 0;
        for (Edge edge : edges) {
            length += edge.length();
        }
        return length;
    }

    /**
     * @return la totalité des arêtes de l'itinéraire
     */
    @Override
    public List<Edge> edges() {
        return this.edges;
    }

    /**
     * @return la totalité des points situés aux extrémités des arêtes de l'itinéraire
     */
    @Override
    public List<PointCh> points() {
        List<PointCh> list = new ArrayList<>();
        for (Edge edge : edges) {
            list.add(edge.fromPoint());
        }
        list.add(edges.get(edges.size() - 1).toPoint());
        return list;
    }

    /**
     * @param position position sur l'itinéraire simple
     * @return le point se trouvant à la position donnée le long de l'itinéraire
     */
    @Override
    public PointCh pointAt(double position) {
        position = Math2.clamp(0, position, this.length());

        int index = Arrays.binarySearch(tab, position);

        if (index > 0) {
            index -= 1;
            position -= tab[index];
            return edges.get(index).pointAt(position);
        }
        if (index == 0) {
            return edges.get(index).pointAt(position);
        }
        index = -index - 2;
        position -= tab[index];
        return edges.get(index).pointAt(position);

    }

    /**
     * @param position où l'atitude se trouve
     * @return l'altitude à la position donnée le long de l'itinéraire,
     * qui peut valoir NaN si l'arête contenant cette position n'a pas de profil
     */
    @Override
    public double elevationAt(double position) {
        position = Math2.clamp(0, position, this.length());

        int index = Arrays.binarySearch(tab, position);

        if (index > 0) {
            index -= 1;
            position -= tab[index];
            return edges.get(index).elevationAt(position);
        }
        if (index == 0) {
            return edges.get(index).elevationAt(position);
        }
        index = -index - 2;
        position -= tab[index];
        return edges.get(index).elevationAt(position);

    }

    /**
     * @param position sur l'itinéraire simple
     * @return l'identité du nœud appartenant à l'itinéraire et se trouvant le plus proche de la position donnée
     */
    @Override
    public int nodeClosestTo(double position) {
        position = Math2.clamp(0, position, this.length());

        int index = Arrays.binarySearch(tab, position);

        if (index > 0) {
            index -= 1;
            position -= tab[index];
            if ((edges.get(index).length() / 2) > position) {
                return edges.get(index).fromNodeId();
            }
            return edges.get(index).toNodeId();
        }
        if (index == 0) {
            return edges.get(index).fromNodeId();
        }
        index = -index - 2;
        position -= tab[index];
        if ((edges.get(index).length() / 2) > position) {
            return edges.get(index).fromNodeId();
        }
        return edges.get(index).toNodeId();


    }

    /**
     * @param point un point de coordonée
     * @return le point de l'itinéraire se trouvant le plus proche du point de référence donné
     */
    @Override
    public RoutePoint pointClosestTo(PointCh point) {
        int index = 0;
        double dtf_min = Double.POSITIVE_INFINITY;

        for (int i = 0; i < edges.size(); ++i) {
            double dtf_max = point.distanceTo(edges.get(i).pointAt
                    (Math2.clamp(0, edges.get(i).positionClosestTo(point), edges.get(i).length())));

            if (dtf_max < dtf_min) {
                dtf_min = dtf_max;
                index = i;
            }
        }

        double min = Math2.clamp(0, edges.get(index).positionClosestTo(point), edges.get(index).length());
        return new RoutePoint(edges.get(index).pointAt(min), min + tab[index], dtf_min);
    }


}
