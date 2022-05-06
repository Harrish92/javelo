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
    private final Consumer<String> erreurs;
    private final RouteBean rb;
    private final Polyline polyline;
    private final Circle cercle;


    public RouteManager(RouteBean rb, ObjectProperty<MapViewParameters> mapViewParametersProperty, Consumer<String> erreurs){
        this.rb = rb;
        this.erreurs = erreurs;
        this.mapViewParametersProperty = mapViewParametersProperty;
        this.pane = new Pane();
        this.cercle = new Circle(5);
        polyline = new Polyline();

        polyline.setId("route");
        cercle.setId("highlight");

        pane.getChildren().add(polyline);
        pane.getChildren().add(cercle);

        pane.setPickOnBounds(false);


        dessiner();



        mapViewParametersProperty.addListener((p, oldS, newS) -> {
            if (oldS.zoomLevel() != newS.zoomLevel()){
                dessiner();
            }else{
                glissementSurLaCarte();
            }


        });

        rb.getRouteProperty().addListener((p, oldS, newS) -> {
            dessiner();
        });

        cercle.setOnMouseClicked(e->{
            ajouterUnWayPoint(e.getX(), e.getY());
        });



    }

    private void ajouterUnWayPoint(double x, double y) {
        Point2D p2d = new Point2D(x, y).add(mapViewParametersProperty.get().topLeft());
        Point2D p2dlocaltoparent = cercle.localToParent(p2d);
        PointWebMercator pwm = PointWebMercator.of(mapViewParametersProperty.get().zoomLevel(),
                p2dlocaltoparent.getX(), p2dlocaltoparent.getY()) ;

        for(Waypoint wpFromTheList : rb.getPointsList()){
            int wayPointNewNodeId = rb.getRouteProperty().get().nodeClosestTo(rb.highlightedPosition());
            if(wpFromTheList.NodeId() != wayPointNewNodeId){
                Waypoint wp = new Waypoint(pwm.toPointCh(),
                        rb.getRouteProperty().get().nodeClosestTo(rb.highlightedPosition()));
                List<Waypoint> wp_list = new ArrayList<>();
                wp_list.add(wp);
                rb.setPointsList(wp_list);
            }else{
                erreurs.accept("Un point de passage est déjà présent à cet endroit !");

            }
        }
    }

    private void glissementSurLaCarte() {
        polyline.setLayoutX(-mapViewParametersProperty.get().coordX());
        polyline.setLayoutY(-mapViewParametersProperty.get().coordY());

        cercle.setLayoutX(-mapViewParametersProperty.get().coordX());
        cercle.setLayoutY(-mapViewParametersProperty.get().coordY());

    }

    private void dessiner(){

        List<Double> doubleArrayList = new ArrayList<>();

        for(PointCh r : rb.getRouteProperty().get().points()){
            PointWebMercator pwm = PointWebMercator.ofPointCh(r);
            doubleArrayList.add(pwm.xAtZoomLevel(mapViewParametersProperty.get().zoomLevel()));
            doubleArrayList.add(pwm.yAtZoomLevel(mapViewParametersProperty.get().zoomLevel()));
        }


        polyline.setVisible(!rb.getPointsList().isEmpty());
        cercle.setVisible(!rb.getPointsList().isEmpty());

        PointCh PositionChDuCercle = rb.getRouteProperty().get().pointAt(rb.highlightedPosition());

        PointWebMercator pwm = PointWebMercator.ofPointCh(PositionChDuCercle);

        polyline.getPoints().setAll(doubleArrayList);

        polyline.setLayoutX(-mapViewParametersProperty.get().coordX());
        polyline.setLayoutY(-mapViewParametersProperty.get().coordY());

        cercle.setCenterX(pwm.xAtZoomLevel(mapViewParametersProperty.get().zoomLevel()));
        cercle.setCenterY(pwm.yAtZoomLevel(mapViewParametersProperty.get().zoomLevel()));

        cercle.setLayoutX(-mapViewParametersProperty.get().coordX());
        cercle.setLayoutY(-mapViewParametersProperty.get().coordY());



    }


    public Pane pane(){
        return this.pane;
    }


}
