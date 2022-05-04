package ch.epfl.javelo.gui;

import ch.epfl.javelo.routing.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import javafx.geometry.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Représente un bean JavaFX regroupant les propriétés
 * relatives aux points de passage et à l'itinéraire correspondant.
 * @author Yoan Giovannini (303934)
 */
public final class RouteBean {
    //TODO: propriétés publiques ?
    public  ObservableList<Waypoint> pointsList;
    public ObjectProperty<Route> routeProperty;
    public DoubleProperty highlightedPosition;
    public ObjectProperty<ElevationProfile> elevationProfile;
    private RouteComputer routeComputer;
    private final int DISTANCEMAX = 5;
    private final int MAXCACHESIZE = 50;
    private LinkedHashMap<RouteSample, Route> cache;

    /**
     * Constructeur par défaut.
     * @param routeComputer un calculateur d'itinéraire.
     */
    public RouteBean(RouteComputer routeComputer){
        this.routeComputer = routeComputer;
        //TODO: check propriétés + 1 seul listener ?
        pointsList.addListener((ListChangeListener<? super Waypoint>) e -> computeRoute());
    }

    /**
     * Représente un segment d'itinéraire caractérisé par
     * son point de départ et d'arrivée.
     * @param nodeId1 le point de départ.
     * @param nodeId2 le point d'arrivée.
     */
    private final record RouteSample(int nodeId1, int nodeId2){}

    /**
     * Calcule l'itinéraire sous forme d'une MultiRoute attribuée
     * à propriété routeProperty. Calcule aussi le profile de l'itinéraire.
     */
    private void computeRoute(){
        ArrayList<Route> routesList = new ArrayList<>();
        for (int k = 0; k < pointsList.size() - 1; k++) {
            int n1 = pointsList.get(k).NodeId();
            int n2 = pointsList.get(k + 1).NodeId();
            RouteSample key = new RouteSample(n1, n2);
            if(cache.containsKey(key)) {
                routesList.add(cache.get(key));
            }
            else{
                Route route = routeComputer.bestRouteBetween(n1 ,n2);
                routesList.add(route);
                cache.put(key, route);
                if(cache.size() > MAXCACHESIZE){
                    cache.remove(cache.entrySet().iterator().next().getKey());
                }
            }
        }
        routeProperty.set( pointsList.size() >= 2 ? new MultiRoute(routesList) : null);
        elevationProfile.set(ElevationProfileComputer.elevationProfile(
                routeProperty.get(),
                DISTANCEMAX));
    }

    //TODO: ajouter des getters setters si besoin
    /**
     * @return la propriété elevationProfile sous la forme
     * d'une ReadOnlyObjectProperty.
     */
    public ReadOnlyObjectProperty<ElevationProfile> elevationProfile(){
        return elevationProfile;
    }

    /**
     * @return la propriété routeProperty sous la forme
     * d'une ReadOnlyObjectProperty.
     */
    public ReadOnlyObjectProperty<Route> routeProperty(){
        return routeProperty;
    }

    /**
     * @return la propriété highlightedPosition de type DoubleProperty.
     */
    public DoubleProperty highlightedPositionProperty(){
        return highlightedPosition;
    }

    /**
     * @return le nombre de type Double contenu dans highlightedPosition.
     */
    public double highlightedPosition(){
        return highlightedPosition.get();
    }

    /**
     * Setter pour highlightedPosition.
     * @param value la valeur à attribuer de type Double.
     */
    public void setHighlightedPosition(Double value){
        highlightedPosition.set(value);
    }

}
