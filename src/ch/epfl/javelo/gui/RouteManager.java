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

public class RouteManager {
    private final Pane pane;
    private final ObjectProperty<MapViewParameters> mapViewParametersProperty;
    private final RouteBean rb;
    private final Polyline polyline;
    private final Circle circle;
    private final double HighlightedPos;


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
            draw();


        mapViewParametersProperty.addListener((p, oldS, newS) -> {
            if (oldS.zoomLevel() != newS.zoomLevel()){
                draw();
            }else{
                slideOnTheMap();
            }

        });


        rb.getRouteProperty().addListener((p, oldS, newS) -> {
            draw();
        });


        circle.setOnMouseClicked(e->{
            addWaypointInTheWhiteCircle(e.getX(), e.getY());
        });





    }

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

    private void slideOnTheMap() {
        if(rb.getRouteProperty().getValue() == null)
            return;
        setLayoutOfPolyline();

        setLayoutOfCircle();

    }

    private void draw(){
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

    private void setLayoutOfPolyline(){
        polyline.setLayoutX(-mapViewParametersProperty.get().coordX());
        polyline.setLayoutY(-mapViewParametersProperty.get().coordY());
    }

    private void setLayoutOfCircle(){
        circle.setLayoutX(-mapViewParametersProperty.get().coordX());
        circle.setLayoutY(-mapViewParametersProperty.get().coordY());
    }

    private void setCenterOfCircle(){
        rb.setHighlightedPosition(HighlightedPos);
        PointCh positionOfCircleInCh = rb.getRouteProperty().get().pointAt(rb.highlightedPosition());
        PointWebMercator pwm = PointWebMercator.ofPointCh(positionOfCircleInCh);
        circle.setCenterX(pwm.xAtZoomLevel(mapViewParametersProperty.get().zoomLevel()));
        circle.setCenterY(pwm.yAtZoomLevel(mapViewParametersProperty.get().zoomLevel()));
    }

    private void setVisibility(boolean bool){
        polyline.setVisible(bool);
        circle.setVisible(bool);
    }


    public Pane pane(){
        return this.pane;
    }


}
