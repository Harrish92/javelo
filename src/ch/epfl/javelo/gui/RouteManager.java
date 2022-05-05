package ch.epfl.javelo.gui;


import ch.epfl.javelo.projection.PointCh;
import ch.epfl.javelo.projection.PointWebMercator;
import javafx.beans.property.ObjectProperty;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class RouteManager {
    private final Pane pane;
    private final ObjectProperty<MapViewParameters> property;
    private final Consumer<String> erreurs;
    private final RouteBean rb;

    public RouteManager(RouteBean rb, ObjectProperty<MapViewParameters> property, Consumer<String> erreurs){
        this.rb = rb;
        this.erreurs = erreurs;
        this.property = property;
        this.pane = new Pane();
        pane.setPickOnBounds(false);

        Circle c = new Circle();
        c.setRadius(5);
        c.setId("highlight");

        Polyline p = new Polyline();
        p.setId("route");


        pane.getChildren().add(p);
        pane.getChildren().add(c);


        c.setVisible(!rb.pointsList.isEmpty());
        p.setVisible(!rb.pointsList.isEmpty());

        List<Double> doubleArrayList = new ArrayList<>();

        for(Waypoint wp : rb.pointsList){
            doubleArrayList.add(wp.PointCH().e());
            doubleArrayList.add(wp.PointCH().n());
        }

        p.getPoints().setAll(doubleArrayList);
        System.out.println(p.getLayoutX());

        property.addListener(e ->{
            draw();
        });

        pane.sceneProperty().addListener((pChanged, oldS, newS) -> {
            assert oldS == null;

        });


    }

    private void draw(){
        /*
        PointWebMercator pwm = PointWebMercator.ofPointCh()
        pane.getChildren().get(0).setLayoutX(property.get().viewX());

         */
    }

    public Pane pane(){
        return this.pane;
    }


}
