package ch.epfl.javelo.gui;


import ch.epfl.javelo.projection.PointCh;
import ch.epfl.javelo.projection.PointWebMercator;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 *
 * RouteManager représente une gestionnaire de route
 *
 * @author Harrishan Raveendran (345291)
 *
 */
public class RouteManager {
    private final Pane pane;
    private final ObjectProperty<MapViewParameters> mapViewParametersProperty;
    private final RouteBean routeBean;
    private final Polyline polyline;
    private final Circle circle;


    /**
     *constructeur construit une polyligne qui représente une route et
     *un cercle qui permet l'ajout de points de passage intermédiaires
     *
     * @param routeBean un bean JavaFX regroupant les propriétés
     * @param mapViewParametersProperty une propriété JavaFX contenant les paramètres de la carte affichée
     */
    public RouteManager(RouteBean routeBean, ObjectProperty<MapViewParameters> mapViewParametersProperty){
        this.routeBean = routeBean;
        this.mapViewParametersProperty = mapViewParametersProperty;
        this.pane = new Pane();
        this.circle = new Circle(5);
        polyline = new Polyline();

        polyline.setId("route");
        circle.setId("highlight");

        pane.getChildren().add(polyline);
        pane.getChildren().add(circle);

        pane.setPickOnBounds(false);


        mapViewParametersProperty.addListener((p, oldS, newS) -> {
            if (oldS.zoomLevel() != newS.zoomLevel()){
                drawRoute();
                drawCircle();
            }else{
                RouteAndCircleAdaptToSliding();
            }

        });

        routeBean.getRouteProperty().addListener((p, oldS, newS) -> {
            drawRoute();
            drawCircle();
        });


        routeBean.highlightedPositionProperty().addListener((p, oldS, newS) -> drawCircle());


        circle.setOnMouseClicked(e-> addWaypointInTheWhiteCircle(e.getX(), e.getY()));

    }

    /**
     * ajout d'un point de passage dans le cercle blanc
     *
     * @param x coordonnée x de la souris
     * @param y coordonnée y de la souris
     */
    private void addWaypointInTheWhiteCircle(double x, double y) {
        if(routeBean.getRouteProperty().getValue() == null)
            return;

        Point2D p2d = new Point2D(x, y).add(mapViewParametersProperty.get().topLeft());
        Point2D p2dLocalToParent = circle.localToParent(p2d);
        PointWebMercator pwm = PointWebMercator.of(mapViewParametersProperty.get().zoomLevel(),
                p2dLocalToParent.getX(), p2dLocalToParent.getY()) ;

        Waypoint wp = new Waypoint(pwm.toPointCh(),
                routeBean.getRouteProperty().get().nodeClosestTo(routeBean.highlightedPosition()));

        int index = routeBean.indexOfNonEmptySegmentAt(routeBean.highlightedPosition()) + 1;
        routeBean.setPoint(index, wp);



    }


    /**
     * la route s'adapte au glissement de la carte
     */
    private void RouteAndCircleAdaptToSliding() {
        if(routeBean.getRouteProperty().getValue() == null)
            return;

        setLayoutOfPolyline();
        setLayoutOfCircle();

    }

    /**
     * dessin de la route et du cercle sur la carte
     */
    private void drawRoute(){
        if(routeBean.getRouteProperty().getValue() == null){
            polyline.setVisible(false);
            return;
        }
        polyline.setVisible(true);

        List<Double> doubleArrayList = new ArrayList<>();

        routeBean.getRouteProperty().get().points()
                .stream()
                .filter(Objects::nonNull)
                .map(PointWebMercator::ofPointCh)
                .forEachOrdered(e->{
                    doubleArrayList.add(e.xAtZoomLevel(mapViewParametersProperty.get().zoomLevel()));
                    doubleArrayList.add(e.yAtZoomLevel(mapViewParametersProperty.get().zoomLevel()));

                });

        polyline.getPoints().setAll(doubleArrayList);

        setLayoutOfPolyline();

    }

    private void drawCircle(){
        if(Double.isNaN(routeBean.highlightedPosition()) || routeBean.getRouteProperty().getValue() == null){
            circle.setVisible(false);
            return;
        }
        circle.setVisible(true);

        setCenterOfCircle();
        setLayoutOfCircle();

    }

    /**
     * translate la position de la polyligne sur le panneau
     */
    private void setLayoutOfPolyline(){
        polyline.setLayoutX(-mapViewParametersProperty.get().coordX());
        polyline.setLayoutY(-mapViewParametersProperty.get().coordY());
    }

    /**
     * translate la position du cercle sur le panneau
     */
    private void setLayoutOfCircle(){
        circle.setLayoutX(-mapViewParametersProperty.get().coordX());
        circle.setLayoutY(-mapViewParametersProperty.get().coordY());
    }

    /**
     * définit le centre du cercle
     */
    private void setCenterOfCircle(){
        PointCh positionOfCircleInCh = routeBean.getRouteProperty().get().pointAt(routeBean.highlightedPosition());
        PointWebMercator pwm = PointWebMercator.ofPointCh(positionOfCircleInCh);
        circle.setCenterX(pwm.xAtZoomLevel(mapViewParametersProperty.get().zoomLevel()));
        circle.setCenterY(pwm.yAtZoomLevel(mapViewParametersProperty.get().zoomLevel()));
    }


    /**
     *
     * @return un panneau
     */
    public Pane pane(){
        return this.pane;
    }


}
