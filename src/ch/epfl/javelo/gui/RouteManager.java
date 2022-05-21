package ch.epfl.javelo.gui;


import ch.epfl.javelo.projection.PointCh;
import ch.epfl.javelo.projection.PointWebMercator;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;

import java.util.ArrayList;
import java.util.List;


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
    private final RouteBean rb;
    private final Polyline polyline;
    private final Circle circle;
    private final double HighlightedPos;


    /**
     *constructeur construit une polyligne qui représente une route et
     *un cercle qui permet l'ajout de points de passage intermédiaires
     *
     * @param rb un bean JavaFX regroupant les propriétés
     * @param mapViewParametersProperty une propriété JavaFX contenant les paramètres de la carte affichée
     */
    public RouteManager(RouteBean rb, ObjectProperty<MapViewParameters> mapViewParametersProperty){
        HighlightedPos = rb.highlightedPosition();
        this.rb = rb;
        this.mapViewParametersProperty = mapViewParametersProperty;
        this.pane = new Pane();
        this.circle = new Circle(5);
        polyline = new Polyline();

        polyline.setId("route");
        circle.setId("highlight");

        pane.getChildren().add(polyline);
        pane.getChildren().add(circle);

        pane.setPickOnBounds(false);

        if(rb.getRouteProperty() != null)
            drawRouteAndCircle();


        mapViewParametersProperty.addListener((p, oldS, newS) -> {
            if (oldS.zoomLevel() != newS.zoomLevel()){
                drawRouteAndCircle();
            }else{
                RouteAndCircleAdaptToSliding();
            }

        });


        rb.getRouteProperty().addListener((p, oldS, newS) -> {
            drawRouteAndCircle();
        });


        circle.setOnMouseClicked(e->{
            addWaypointInTheWhiteCircle(e.getX(), e.getY());
        });





    }

    /**
     * ajout d'un point de passage dans le cercle blanc
     *
     * @param x coordonnée x de la souris
     * @param y coordonnée y de la souris
     */
    private void addWaypointInTheWhiteCircle(double x, double y) {
        if(rb.getRouteProperty().getValue() == null)
            return;
        Point2D p2d = new Point2D(x, y).add(mapViewParametersProperty.get().topLeft());
        Point2D p2dLocalToParent = circle.localToParent(p2d);
        PointWebMercator pwm = PointWebMercator.of(mapViewParametersProperty.get().zoomLevel(),
                p2dLocalToParent.getX(), p2dLocalToParent.getY()) ;

        Waypoint wp = new Waypoint(pwm.toPointCh(),
                rb.getRouteProperty().get().nodeClosestTo(rb.highlightedPosition()));

        int index = rb.indexOfNonEmptySegmentAt(rb.highlightedPosition()) + 1;
        rb.setPoint(index, wp);



    }


    /**
     * la route s'adapte au glissement de la carte
     */
    private void RouteAndCircleAdaptToSliding() {
        if(rb.getRouteProperty().getValue() == null)
            return;
        setLayoutOfPolyline();

        setLayoutOfCircle();

    }

    /**
     * dessin de la route et du cercle sur la carte
     */
    private void drawRouteAndCircle(){
        if(rb.getRouteProperty().getValue() == null){
            setVisibility(false);
            return;
        }
        setVisibility(true);

        List<Double> doubleArrayList = new ArrayList<>();

        if(rb.getRouteProperty().get().points() != null) {
            for (PointCh r : rb.getRouteProperty().get().points()) {

                PointWebMercator pwm = PointWebMercator.ofPointCh(r);
                doubleArrayList.add(pwm.xAtZoomLevel(mapViewParametersProperty.get().zoomLevel()));
                doubleArrayList.add(pwm.yAtZoomLevel(mapViewParametersProperty.get().zoomLevel()));

            }
        }

        polyline.getPoints().setAll(doubleArrayList);

        setLayoutOfPolyline();

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
        rb.setHighlightedPosition(HighlightedPos);
        PointCh positionOfCircleInCh = rb.getRouteProperty().get().pointAt(rb.highlightedPosition());
        PointWebMercator pwm = PointWebMercator.ofPointCh(positionOfCircleInCh);
        circle.setCenterX(pwm.xAtZoomLevel(mapViewParametersProperty.get().zoomLevel()));
        circle.setCenterY(pwm.yAtZoomLevel(mapViewParametersProperty.get().zoomLevel()));
    }

    /**
     * définit la visibilité du cercle et de la polyligne sur la carte
     *
     * @param bool valeur booléenne
     */
    private void setVisibility(boolean bool){
        polyline.setVisible(bool);
        circle.setVisible(bool);
    }


    /**
     *
     * @return un panneau
     */
    public Pane pane(){
        return this.pane;
    }


}
