package ch.epfl.javelo.gui;


import ch.epfl.javelo.projection.PointWebMercator;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.Group;
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
    private Polyline polyline;
    private Circle cercle;
    private Group group;

    public RouteManager(RouteBean rb, ObjectProperty<MapViewParameters> mapViewParametersProperty, Consumer<String> erreurs){
        this.rb = rb;
        this.erreurs = erreurs;
        this.mapViewParametersProperty = mapViewParametersProperty;
        this.pane = new Pane();
        this.cercle = new Circle();

        pane.setPickOnBounds(false);
        group = new Group();


        dessiner();


        mapViewParametersProperty.addListener((p, oldS, newS) -> {
            if (oldS.zoomLevel() != newS.zoomLevel()){
                dessiner();
            }else{
                glissementSurLaCarte(oldS.topLeft(), newS.topLeft());
            }

        });


    }


    private void glissementSurLaCarte(Point2D olds, Point2D news) {

        polyline.setLayoutX(news.getX());
        polyline.setLayoutY(news.getY());
        System.out.println(polyline.getLayoutX());
    }

    private void dessiner(){
        pane.getChildren().removeAll(pane.getChildren());
        group.getChildren().removeAll(group.getChildren());

        cercle.setRadius(5);
        cercle.setId("highlight");

        pane.getChildren().add(cercle);


        List<Double> doubleArrayList = new ArrayList<>();
        polyline = new Polyline();

        polyline.setId("route");

        for(Waypoint wp : rb.getPointsList()){
            PointWebMercator pwm = PointWebMercator.ofPointCh(wp.PointCH());


            doubleArrayList.add(mapViewParametersProperty.get().viewX(pwm));
            doubleArrayList.add(mapViewParametersProperty.get().viewY(pwm));
        }

        polyline.getPoints().setAll(doubleArrayList);
        polyline.setVisible(!rb.getPointsList().isEmpty());


        group.getChildren().add(polyline);
        pane.getChildren().add(group);
    }


    public Pane pane(){
        return this.pane;
    }


}
