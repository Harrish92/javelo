package ch.epfl.javelo.gui;


import ch.epfl.javelo.data.Graph;
import ch.epfl.javelo.projection.PointCh;
import ch.epfl.javelo.routing.RoutePoint;
import javafx.beans.property.*;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.function.Consumer;

/**
 * Gère l'affichage de la carte annotée.
 *
 * @author Yoan Giovannini (303934)
 */
public final class AnnotatedMapManager {
    private final int ZOOMLEVEL = 12;
    private final double STARTX = 543200;
    private final double STARTY = 370650;
    private final int NAN = (int) Double.NaN;
    private final int MAXDISTANCEROUTE = 15;
    private Point2D mousePosition;
    private final Pane pane;
    private final ObjectProperty<MapViewParameters> mapViewParametersProperty;
    private final RouteBean routeBean;

    public AnnotatedMapManager(Graph graph, TileManager tileManager,
                               RouteBean routeBean, Consumer<String> errorConsumer){
        mapViewParametersProperty = new SimpleObjectProperty<>(new MapViewParameters(
                                                                ZOOMLEVEL,
                                                                STARTX,
                                                                STARTY));
        WaypointsManager waypointsManager = new WaypointsManager(graph,
                                            mapViewParametersProperty,
                                            routeBean.getPointsList(),
                                            errorConsumer);
        BaseMapManager baseMapManager = new BaseMapManager(tileManager,
                                                            waypointsManager,
                                                            mapViewParametersProperty);
        RouteManager routeManager = new RouteManager(routeBean,
                                                    mapViewParametersProperty);
        this.routeBean = routeBean;
        pane = new StackPane(
                baseMapManager.pane(),
                routeManager.pane(),
                waypointsManager.pane());
        pane.getStylesheets().add("map.css");

        mousePosition = new Point2D(NAN,NAN);
        baseMapManager.pane().setOnMouseMoved(e ->
                mousePosition = new Point2D(e.getSceneX(),e.getSceneY()));
        baseMapManager.pane().setOnMouseExited(e ->
                mousePosition = new Point2D(NAN,NAN));


    }

    /**
     * Retourne le panneau pane initialisé dans le constructeur.
     * @return un panneau pane.
     */
    public Pane pane(){return pane;}

    /**
     * Retourne la position de la souris dans une propriété en lecture seule.
     * @return la position de la souris.
     */
    public ReadOnlyIntegerProperty mousePositionOnRouteProperty(){
        IntegerProperty mousePositionOnRouteProperty = new SimpleIntegerProperty(NAN);
        if(routeBean.getRouteProperty().get() != null) {
            PointCh mousePosCh = mapViewParametersProperty.get().pointAt(mousePosition.getX(), mousePosition.getY()).toPointCh();
            RoutePoint routePos = routeBean.getRouteProperty().get().pointClosestTo(mousePosCh);
            if (mousePosCh.distanceTo(routePos.point()) <= MAXDISTANCEROUTE) {
                mousePositionOnRouteProperty.set((int) routePos.position());
            }
        }
        return mousePositionOnRouteProperty;
    }


}
