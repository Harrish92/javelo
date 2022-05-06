package ch.epfl.javelo.gui;

import ch.epfl.javelo.routing.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import javafx.geometry.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Représente un bean JavaFX regroupant les propriétés
 * relatives aux points de passage et à l'itinéraire correspondant.
 * @author Yoan Giovannini (303934)
 */
public final class RouteBean {
    //TODO: propriétés publiques ?
    private final  ObservableList<Waypoint> pointsList;
    private final ObjectProperty<Route> routeProperty = new SimpleObjectProperty<>();
    private final DoubleProperty highlightedPosition = new SimpleDoubleProperty();
    private final ObjectProperty<ElevationProfile> elevationProfile = new SimpleObjectProperty<>();
    private final RouteComputer routeComputer;
    private final int DISTANCEMAX = 5;
    private final int MAXCACHESIZE = 50;
    private final LinkedHashMap<RouteSample, Route> cache = new LinkedHashMap<>();

    /**
     * Constructeur par défaut.
     * @param routeComputer un calculateur d'itinéraire.
     */
    public RouteBean(RouteComputer routeComputer){
        pointsList = FXCollections.observableList(new ArrayList<>());
        this.routeComputer = routeComputer;
        //TODO: check propriétés + 1 seul listener ?
        pointsList.addListener((ListChangeListener<? super Waypoint>) e ->{computeRoute();});

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
        boolean isRouteValid = true;
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
            isRouteValid = !routesList.get(k).equals(null);
        }
        routeProperty.set( pointsList.size() >= 2 && isRouteValid ? new MultiRoute(routesList) : null);
        if(routeProperty.get() == null) {highlightedPosition.set(Double.NaN);}
        else{elevationProfile.set(ElevationProfileComputer.elevationProfile(
                routeProperty.get(),
                DISTANCEMAX));}
    }

    //TODO: ajouter des getters setters si besoin
    /**
     * @return la propriété elevationProfile sous la forme
     * d'une ReadOnlyObjectProperty.
     */
    public ReadOnlyObjectProperty<ElevationProfile> elevationProfile(){
        return elevationProfile;
    }

    public void setRouteProperty(ObjectProperty<Route> routeP){
        routeProperty.set(routeP.get());
    }

    /**
     * @return la propriété routeProperty sous la forme
     * d'une ReadOnlyObjectProperty.
     */
    public ReadOnlyObjectProperty<Route> getRouteProperty(){
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

    public void setPointsList(List<Waypoint> l){pointsList.addAll(l);}

    public void setPoint(Waypoint point){pointsList.add(point);}

    public ObservableList<Waypoint> getPointsList(){return  pointsList;}


}
