package ch.epfl.javelo.routing;

import ch.epfl.javelo.Math2;
import ch.epfl.javelo.Preconditions;
import ch.epfl.javelo.projection.PointCh;

import java.util.ArrayList;
import java.util.List;

/**
 * Multiroute représente un itinéraire multiple
 *
 * @author Harrishan Raveendran (345291)
 */

public class MultiRoute implements Route {
    private final List<Route> segments;

    /**
     * @param segments liste de routes
     */
    public MultiRoute(List<Route> segments) {
        Preconditions.checkArgument(!segments.isEmpty());
        this.segments = List.copyOf(segments);
    }

    /**
     * @param position position sur le segment
     * @return l'index du segment de l'itinéraire contenant la position donnée
     */

    @Override
    public int indexOfSegmentAt(double position) {
        position = Math2.clamp(0, position, this.length());

        int index = 0;
        for (Route segment : segments) {
            if (segment.length() < position) {
                index += segment.indexOfSegmentAt(segment.length()) + 1;
                position -= segment.length();
            } else {
                index += segment.indexOfSegmentAt(position);
                break;
            }
        }
        return index;
    }

    /**
     * @return la longueur de l'itinéraire
     */
    @Override
    public double length() {
        double length = 0;
        for (Route segment : segments) {
            length += segment.length();
        }
        return length;
    }

    /**
     * @return tous des arêtes de l'itinéraire
     */
    @Override
    public List<Edge> edges() {
        List<Edge> list_edge = new ArrayList<>();
        for (Route segment : segments) {
            list_edge.addAll(segment.edges());
        }
        return list_edge;
    }

    /**
     * @return retourne tous les points situés aux extrémités des arêtes de l'itinéraire,
     * sans doublons
     */
    @Override
    public List<PointCh> points() {
        List<PointCh> list_pch = new ArrayList<>();

        for (Route segment : segments) {

            for (PointCh points : segment.points()) {
                if (list_pch.size() != 0) {
                    if (!list_pch.get(list_pch.size() - 1).equals(points)) {
                        list_pch.add(points);
                    }
                } else {
                    list_pch.add(segment.points().get(0));
                }

            }
        }

        return list_pch;
    }

    /**
     * @param position sur le long de l'itinéraire
     * @return le point se trouvant à la position donnée le long de l'itinéraire
     */
    @Override
    public PointCh pointAt(double position) {
        position = Math2.clamp(0, position, this.length());
        for (Route segment : segments) {
            if (segment.length() < position) {
                position -= segment.length();
            } else {
                return segment.pointAt(position);
            }
        }
        return pointAt(position);
    }

    /**
     * @param position sur le long de l'itinéraire
     * @return l'altitude à la position donnée le long de l'itinéraire,
     * qui peut valoir NaN si l'arête contenant cette position n'a pas de profil
     */
    @Override
    public double elevationAt(double position) {
        position = Math2.clamp(0, position, this.length());
        for (Route segment : segments) {
            if (segment.length() < position) {
                position -= segment.length();
            } else {
                return segment.elevationAt(position);
            }
        }
        return elevationAt(position);

    }

    /**
     * @param position sur le long de l'itinéraire
     * @return l'identité du nœud appartenant à l'itinéraire et se trouvant le plus proche de la position donnée
     */
    @Override
    public int nodeClosestTo(double position) {
        position = Math2.clamp(0, position, this.length());
        for (Route segment : segments) {
            if (segment.length() < position) {
                position -= segment.length();
            } else {
                return segment.nodeClosestTo(position);
            }
        }
        return nodeClosestTo(position);
    }

    /**
     * @param point sur le long de l'itinéraire
     * @return le point de l'itinéraire se trouvant le plus proche du point de référence donné
     */
    @Override
    public RoutePoint pointClosestTo(PointCh point) {

        double longueur = 0;
        RoutePoint ShortestDist = RoutePoint.NONE;
        for (Route segment : segments) {
            ShortestDist = ShortestDist
                    .min(segment.pointClosestTo(point)
                            .withPositionShiftedBy(longueur));

            longueur += segment.length();
        }
        return ShortestDist;
    }


}
