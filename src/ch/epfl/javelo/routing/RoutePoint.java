package ch.epfl.javelo.routing;

import ch.epfl.javelo.projection.PointCh;


/**
 * RoutePoint représente le point d'un itinéraire le plus proche d'un point de référence donné
 *
 * @author Harrishan Raveendran (345291)
 *
 *
 * @param point sur l'itinéraire
 * @param position du point le long de l'itinéraire
 * @param distanceToReference la distance entre le point et la référence
 */
public record RoutePoint(PointCh point, double position, double distanceToReference) {
    public static final RoutePoint NONE = new RoutePoint(null, Double.NaN, Double.POSITIVE_INFINITY);

    /**
     *
     * @param positionDifference différence
     * @return nouvelle instance de Routepoint identique à this,
     * mais dont la position est décalée de la différence donnée,
     * qui peut être positive ou négative
     */
    public RoutePoint withPositionShiftedBy(double positionDifference){
        return new RoutePoint(point, position + positionDifference, distanceToReference);
    }

    /**
     *
     * @param that instance de RoutePoint
     * @return this si sa distance à la référence est inférieure ou égale à celle de that (autre RoutePoint),
     * sinon retourn that
     */
    public RoutePoint min(RoutePoint that){
        return this.distanceToReference <= that.distanceToReference ? this : that;
    }

    /**
     *
     * @param thatPoint le point sur l'itinéraire de l'instance de RoutePoint
     * @param thatPosition la position du point le long de l'itinéraire,cde l'instance de RoutePoint
     * @param thatDistanceToReference la distance entre le point et la référence de l'instance de RoutePoint
     *
     * @return this si sa distance à la référence est inférieure ou égale à thatDistanceToReference,
     * sinon retourne une nouvelle instance de RoutePoint dont les attributs sont les arguments passés à min .
     */
    public RoutePoint min(PointCh thatPoint, double thatPosition, double thatDistanceToReference){
        return this.distanceToReference <= thatDistanceToReference ?
                this : new RoutePoint(thatPoint, thatPosition, thatDistanceToReference);
    }







}
