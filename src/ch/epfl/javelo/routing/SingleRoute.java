package ch.epfl.javelo.routing;

import ch.epfl.javelo.Math2;
import ch.epfl.javelo.Preconditions;
import ch.epfl.javelo.projection.PointCh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Harrishan Raveendran (345291)
 *
 */

public final class SingleRoute implements Route {
    private final List<Edge> edges;
    private final double[] tab;

    /**
     *
     * @param edges liste cone
     *  Constructeur public qui retourne l'itinéraire simple composé des arêtes données,
     *  ou lève IllegalArgumentException si la liste d'arêtes est vide.
     */
    public SingleRoute(List<Edge> edges){
        Preconditions.checkArgument(!edges.isEmpty());
        this.edges = edges;


        tab = new double[edges.size()+1]; // 6 elements
        double longueur = 0;
        tab[0] = longueur;

        for(int i=0; i < this.edges.size(); i++){
            longueur += this.edges.get(i).length();
            tab[i+1] = longueur;
        }

    }

    /** Constructeur de copie */

    public SingleRoute(SingleRoute that){
        this(that.edges);
    }

    /**
     *
     * @param position position de l'arête
     * @return l'index du segment de l'itinéraire contenant la position donnée,
     * qui vaut toujours 0 dans le cas d'un itinéraire simple
     */
    @Override
    public int indexOfSegmentAt(double position) {
        return 0;
    }

    /**
     *
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
     *
     * @return la totalité des arêtes de l'itinéraire
     */
    @Override
    public List<Edge> edges() {
        return this.edges;
    }

    /**
     *
     * @return la totalité des points situés aux extrémités des arêtes de l'itinéraire
     */
    @Override
    public List<PointCh> points() {
        List<PointCh> list  = new ArrayList<>();
        for (Edge edge : edges) {
            list.add(edge.fromPoint());
        }
        list.add(edges.get(edges.size()-1).toPoint());
        return list;
    }

    /**
     *
     * @param position position sur l'arête
     * @return le point se trouvant à la position donnée le long de l'itinéraire
     */
    @Override
    public PointCh pointAt(double position) {
        position = Math2.clamp(0, position, this.length());

        int index = Arrays.binarySearch(tab, position);

        if(index > 0){
            index -= 1;
            position -= tab[index];
            return edges.get(index).pointAt(position);
        }
        if(index == 0){
            return edges.get(index).pointAt(position);
        }
        index = -index - 2;
        position-= tab[index];
        return edges.get(index).pointAt(position);

    }

    /**
     *
     * @param position où l'atitude se trouve
     * @return l'altitude à la position donnée le long de l'itinéraire,
     * qui peut valoir NaN si l'arête contenant cette position n'a pas de profil
     */
    @Override
    public double elevationAt(double position) {
        position = Math2.clamp(0, position, this.length());

        int index = Arrays.binarySearch(tab, position);

        if(index > 0){
            index -= 1;
            position -= tab[index];
            return edges.get(index).elevationAt(position);
        }
        if(index == 0){
            return edges.get(index).elevationAt(position);
        }
        index = -index - 2;
        position-= tab[index];
        return edges.get(index).elevationAt(position);

        /*
        if(index >= 0){
            return edges.get(index).elevationAt(position);
        }
        int new_index = -index - 1;
        if(new_index == 0){
            return edges.get(new_index).elevationAt(position);
        }else{
            return edges.get(new_index-1).elevationAt(position);
        }

         */

    }

    /**
     *
     * @param position sur l'arête
     * @return  l'identité du nœud appartenant à l'itinéraire et se trouvant le plus proche de la position donnée
     */
    @Override
    public int nodeClosestTo(double position) {
        position = Math2.clamp(0, position, this.length());

        int index = Arrays.binarySearch(tab, position);

        if(index > 0){
            index -= 1;
            return edges.get(index).fromNodeId();
        }
        if(index == 0){
            return edges.get(index).fromNodeId();
        }
        index = -index - 2;
        return edges.get(index).fromNodeId();

        /*
        int index = Arrays.binarySearch(tab, position);
        if(index >= 0){
            return edges.get(index).fromNodeId();
        }
        int new_index = -index - 1;
        if(new_index == 0){
            return edges.get(new_index).fromNodeId();
        }else{
            return edges.get(new_index-1).fromNodeId();
        }

         */

    }

    /**
     *
     * @param point un point de coordonée
     * @return le point de l'itinéraire se trouvant le plus proche du point de référence donné
     */
    @Override
    public RoutePoint pointClosestTo(PointCh point) {
        int index = 0;
        double dtf_min = point.distanceTo(edges.get(0).pointAt(edges.get(0).positionClosestTo(point)));

        for(int i=1; i < edges.size(); ++i){
            double dtf_max = point.distanceTo(edges.get(i).pointAt(edges.get(i).positionClosestTo(point)));
            if(dtf_max < dtf_min) {
                dtf_min = dtf_max;
                index = i;
            }
        }
        double min = edges.get(index).positionClosestTo(point);
        PointCh pch = edges.get(index).pointAt(min);
        double position = min + tab[index];
        return new RoutePoint(pch, position, dtf_min);
    }
}
