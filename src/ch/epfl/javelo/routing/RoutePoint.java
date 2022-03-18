package ch.epfl.javelo.routing;

import ch.epfl.javelo.projection.PointCh;


/**
 * @author Harrishan Raveendran (345291)
 *
 * @param point le point sur l'itinéraire
 * @param position la position du point le long de l'itinéraire, en mètres
 * @param distanceToReference la distance, en mètres, entre le point et la référence
 */
public record RoutePoint(PointCh point, double position, double distanceToReference) {
    public static final RoutePoint NONE = new RoutePoint(null, Double.NaN, Double.POSITIVE_INFINITY);

    /**
     *
     * @param positionDifference différence
     * @return un point identique au récepteur (this) mais dont la position est décalée de la différence donnée,
     * qui peut être positive ou négative
     */
    RoutePoint withPositionShiftedBy(double positionDifference){
        return new RoutePoint(point, position + positionDifference, distanceToReference);
    }

    /**
     *
     * @param that instance de RoutePoint
     * @return this si sa distance à la référence est inférieure ou égale à celle de that, et that sinon
     */
    RoutePoint min(RoutePoint that){
        if(this.distanceToReference <= that.distanceToReference){
            return this;
        }else{
            return that;
        }
    }

    /**
     *
     * @param thatPoint le point sur l'itinéraire de l'instance de RoutePoint
     * @param thatPosition la position du point le long de l'itinéraire, en mètres de l'instance de RoutePoint
     * @param thatDistanceToReference la distance, en mètres, entre le point et la référence de l'instance de RoutePoint
     *
     * @return this si sa distance à la référence est inférieure ou égale à thatDistanceToReference,
     * et une nouvelle instance de RoutePoint dont les attributs sont les arguments passés à min sinon.
     */
    RoutePoint min(PointCh thatPoint, double thatPosition, double thatDistanceToReference){
        if(this.distanceToReference <= thatDistanceToReference){
            return this;
        }else{
            return new RoutePoint(thatPoint, thatPosition, thatDistanceToReference);
        }
    }







}
