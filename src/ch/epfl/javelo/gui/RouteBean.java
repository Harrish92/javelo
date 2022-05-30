package ch.epfl.javelo.gui;

import ch.epfl.javelo.routing.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Représente un bean JavaFX regroupant les propriétés
 * relatives aux points de passage et à l'itinéraire correspondant.
 * @author Yoan Giovannini (303934)
 */
public final class RouteBean {
    private final  ObservableList<Waypoint> pointsList;
    private final ObjectProperty<Route> routeProperty;
    private final DoubleProperty highlightedPosition;
    private final ObjectProperty<ElevationProfile> elevationProfile;
    private final RouteComputer routeComputer;
    private final int DISTANCEMAX = 5;
    private final int MAXCACHESIZE = 50;
    private final LinkedHashMap<RouteSample, Route> cache;

    /**
     * Constructeur par défaut.
     * @param routeComputer un calculateur d'itinéraire.
     */
    public RouteBean(RouteComputer routeComputer){
        pointsList = FXCollections.observableList(new ArrayList<>());
        routeProperty = new SimpleObjectProperty<>();
        highlightedPosition = new SimpleDoubleProperty();
        elevationProfile = new SimpleObjectProperty<>();
        cache = new LinkedHashMap<>();
        this.routeComputer = routeComputer;
        pointsList.addListener((ListChangeListener<? super Waypoint>) e ->computeRoute());

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
    private void computeRoute() {
        ArrayList<Route> routesList = new ArrayList<>();
        boolean isRouteValid = true;
        for (int k = 0; k < pointsList.size() - 1; k++) {
            int n1 = pointsList.get(k).nodeId();
            int n2 = pointsList.get(k + 1).nodeId();
            if (n1 != n2) {
                RouteSample key = new RouteSample(n1, n2);
                if (cache.containsKey(key)) {
                    routesList.add(cache.get(key));
                } else {
                    Route route = routeComputer.bestRouteBetween(n1, n2);
                    routesList.add(route);
                    cache.put(key, route);
                    if (cache.size() > MAXCACHESIZE) {
                        cache.remove(cache.entrySet().iterator().next().getKey());
                    }
                }
            }
        }

        for (Route route : routesList) {
            if (isRouteValid) isRouteValid = !(route == null);
        }

        routeProperty.set((pointsList.size() >= 2 && isRouteValid) ? new MultiRoute(routesList) : null);
        if (routeProperty.get() == null) {
            elevationProfile.set(null);
        } else {
        elevationProfile.set(ElevationProfileComputer.elevationProfile(
                routeProperty.get(),
                DISTANCEMAX));
        }

    }



    /**
     * @return la propriété elevationProfile sous la forme
     * d'une ReadOnlyObjectProperty.
     */
    public ReadOnlyObjectProperty<ElevationProfile> elevationProfile(){
        return elevationProfile;
    }

    /*public void setRouteProperty(ObjectProperty<Route> routeP){
        routeProperty.set(routeP.get());
    }*/ //probablement inutile


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

    /**
     * Setter pour pointList.
     * @param l une liste de points.
     */
    public void setPointsList(List<Waypoint> l){
        pointsList.addAll(l);}

    /**
     * Ajoute un point à pointList.
     * @param point le point.
     */
    public void setPoint(int index, Waypoint point){
        pointsList.add(index, point);}

    /**
     * Getter pour la liste de points.
     * @return la liste de points.
     */
    public ObservableList<Waypoint> getPointsList(){return  pointsList;}

    public int indexOfNonEmptySegmentAt(double position) {
        int index = routeProperty.get().indexOfSegmentAt(position);
        for (int i = 0; i <= index; i += 1) {
            int n1 = pointsList.get(i).nodeId();
            int n2 = pointsList.get(i + 1).nodeId();
            if (n1 == n2) index += 1;
        }
        return index;
    }


}
