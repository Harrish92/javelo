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

    public RouteManager(RouteBean rb, ObjectProperty<MapViewParameters> property, Consumer<String> erreurs){

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

        for(PointCh pch : rb.routeProperty.get().points()){
            PointWebMercator pwm = PointWebMercator.ofPointCh(pch);
            doubleArrayList.add(pwm.x());
            doubleArrayList.add(pwm.y());
        }


        p.getPoints().setAll(doubleArrayList);


        pane.sceneProperty().addListener((pChanged, oldS, newS) -> {
            assert oldS == null;

        });


    }

    public Pane pane(){
        return this.pane;
    }


}
