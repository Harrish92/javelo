package ch.epfl.javelo.gui;

import ch.epfl.javelo.projection.PointCh;

/**
 *
 * WayPoint représente un point de passage. Il possède deux attributs : Un point CH et l'identité d'un noeud.
 *
 * @author Harrishan Raveendran (345291)
 *
 */
public record Waypoint(PointCh pointCh, int nodeId) {

}
