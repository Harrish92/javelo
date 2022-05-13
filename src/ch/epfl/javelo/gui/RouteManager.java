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
import java.util.function.Consumer;

public class RouteManager {
    private final Pane pane;
    private final ObjectProperty<MapViewParameters> mapViewParametersProperty;
    private final Consumer<String> error;
    private final RouteBean rb;
    private final Polyline polyline;
    private final Circle circle;
    private double HighlightedPos;


    public RouteManager(RouteBean rb, ObjectProperty<MapViewParameters> mapViewParametersProperty, Consumer<String> error){
        HighlightedPos = rb.highlightedPosition();
        this.rb = rb;
        this.error = error;
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
                addWaypoint(e.getX(), e.getY());
        });





    }

    private void addWaypoint(double x, double y) {
        if(rb.getRouteProperty().getValue() == null)
            return;
        Point2D p2d = new Point2D(x, y).add(mapViewParametersProperty.get().topLeft());
        Point2D p2dLocalToParent = circle.localToParent(p2d);
        PointWebMercator pwm = PointWebMercator.of(mapViewParametersProperty.get().zoomLevel(),
                p2dLocalToParent.getX(), p2dLocalToParent.getY()) ;



        int WpHighligthedPosNodeId = rb.getRouteProperty().get().nodeClosestTo(rb.highlightedPosition());
        for(Waypoint wpFromTheList : rb.getPointsList()){
            if(wpFromTheList.NodeId() == WpHighligthedPosNodeId) {
                error.accept("Un point de passage est déjà présent à cet endroit !");
                return;
            }
        }
        Waypoint wp = new Waypoint(pwm.toPointCh(),
                rb.getRouteProperty().get().nodeClosestTo(rb.highlightedPosition()));

        int index = rb.getRouteProperty().get().indexOfSegmentAt(rb.highlightedPosition()) + 1;
        rb.setPoint(index, wp);



    }

    private void slideOnTheMap() {
        if(rb.getRouteProperty().getValue() == null)
            return;
        polyline.setLayoutX(-mapViewParametersProperty.get().coordX());
        polyline.setLayoutY(-mapViewParametersProperty.get().coordY());


        circle.setLayoutX(-mapViewParametersProperty.get().coordX());
        circle.setLayoutY(-mapViewParametersProperty.get().coordY());



    }

    private void draw(){
        if(rb.getRouteProperty().getValue() == null){
            polyline.setVisible(false);
            circle.setVisible(false);
            return;
        }
        polyline.setVisible(true);
        circle.setVisible(true);

        List<Double> doubleArrayList = new ArrayList<>();

        for(PointCh r : rb.getRouteProperty().get().points()){
            PointWebMercator pwm = PointWebMercator.ofPointCh(r);
            doubleArrayList.add(pwm.xAtZoomLevel(mapViewParametersProperty.get().zoomLevel()));
            doubleArrayList.add(pwm.yAtZoomLevel(mapViewParametersProperty.get().zoomLevel()));
        }

        rb.setHighlightedPosition(HighlightedPos);
        PointCh positionOfCircleInCh = rb.getRouteProperty().get().pointAt(rb.highlightedPosition());

        PointWebMercator pwm = PointWebMercator.ofPointCh(positionOfCircleInCh);

        polyline.getPoints().setAll(doubleArrayList);

        polyline.setLayoutX(-mapViewParametersProperty.get().coordX());
        polyline.setLayoutY(-mapViewParametersProperty.get().coordY());

        circle.setCenterX(pwm.xAtZoomLevel(mapViewParametersProperty.get().zoomLevel()));
        circle.setCenterY(pwm.yAtZoomLevel(mapViewParametersProperty.get().zoomLevel()));

        circle.setLayoutX(-mapViewParametersProperty.get().coordX());
        circle.setLayoutY(-mapViewParametersProperty.get().coordY());




    }


    public Pane pane(){
        return this.pane;
    }


}
