package ch.epfl.javelo.routing;

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
    private List<Edge> edges;

    /**
     *
     * @param edges liste cone
     *  Constructeur public qui retourne l'itinéraire simple composé des arêtes données,
     *  ou lève IllegalArgumentException si la liste d'arêtes est vide.
     */
    public SingleRoute(List<Edge> edges){
        Preconditions.checkArgument(!edges.isEmpty());
        this.edges = edges;
        //new SingleRoute(edges);


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
        for(int i=1; i < edges.size(); ++i){
            length += Math.abs((double) (edges.get(i).length() - edges.get(i-1).length()));
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
        for(int i=0; i < edges.size(); ++i){
            list.add(edges.get(i).fromPoint());
            list.add(edges.get(i).toPoint());
        }
        return list;
    }

    /**
     *
     * @param position position sur l'arête
     * @return le point se trouvant à la position donnée le long de l'itinéraire
     */
    @Override
    public PointCh pointAt(double position) {
        double[] tab = new double[edges.size()+1];
        double longueur = 0;
        tab[0] = longueur;

        if(position < 0)
            position = 0;
        if(position > this.length())
            position = this.length();

        for(int i=0; i < edges.size(); ++i){
            longueur += edges.get(i).length();
            tab[i+1] = longueur;
        }
        int index = Arrays.binarySearch(tab, position);

        if(index == 0){
            return edges.get(index).pointAt(position);
        }
        if(index > 0){
            return edges.get(index-1).pointAt(position);
        }else{
            index = -index -1;
            return edges.get(index-1).pointAt(position);
        }
    }

    /**
     *
     * @param position où l'atitude se trouve
     * @return l'altitude à la position donnée le long de l'itinéraire,
     * qui peut valoir NaN si l'arête contenant cette position n'a pas de profil
     */
    @Override
    public double elevationAt(double position) {
        if(position < 0)
            position = 0;
        if(position > this.length())
            position = this.length();

        return 0d;

    }

    /**
     *
     * @param position
     * @return
     */
    @Override
    public int nodeClosestTo(double position) {
        if(position < 0)
            position = 0;
        if(position > this.length())
            position = this.length();

        return 0;
    }

    @Override
    public RoutePoint pointClosestTo(PointCh point) {
        return null;
    }
}
