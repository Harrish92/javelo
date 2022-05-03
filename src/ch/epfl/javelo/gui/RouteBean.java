package ch.epfl.javelo.gui;

import ch.epfl.javelo.routing.ElevationProfile;
import ch.epfl.javelo.routing.Route;
import ch.epfl.javelo.routing.RouteComputer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;

public final class RouteBean {
    public  ObservableList<Waypoint> pointsList;
    public ReadOnlyObjectProperty<Route> routeProperty;
    public DoubleProperty highlightedPosition;
    public ReadOnlyObjectProperty<ElevationProfile> elevationProfile;

    public RouteBean(RouteComputer routeComputer){

    }
}
