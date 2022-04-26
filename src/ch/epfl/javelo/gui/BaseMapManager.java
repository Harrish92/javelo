package ch.epfl.javelo.gui;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.Pane;

public final class BaseMapManager {
    private final TileManager tm;
    private final ObjectProperty<MapViewParameters> property;
    private boolean redrawNeeded;


    public BaseMapManager(TileManager tm /*, WaypointManager wpm */, ObjectProperty<MapViewParameters> property){
        redrawNeeded = false;
        this.tm = tm;
        this.property = property;
        Pane pane = pane();
        Canvas canvas = new Canvas();
        assert pane != null;
        canvas.widthProperty().bind(pane.widthProperty());
        canvas.getGraphicsContext2D();

        canvas.sceneProperty().addListener((p, oldS, newS) -> {
            assert oldS == null;
            newS.addPreLayoutPulseListener(this::redrawIfNeeded);
        });

    }

    public Pane pane(){
        Pane pane = new Pane();
        /*
        pane.setOnScroll(e ->{
            int zooming =  (int) (e.getDeltaY() + 0.5);
            int zoomLevel = property.get().zoomLevel() + zooming;
            property.set(new MapViewParameters(zoomLevel, property.get().CoordX(), property.get().CoordY()));
        });

         */

        return pane;

    }

    private void redrawIfNeeded() {
        if (!redrawNeeded) return;
        redrawNeeded = false;

        // … à faire : dessin de la carte
    }

    private void redrawOnNextPulse() {
        redrawNeeded = true;
        Platform.requestNextPulse();
    }

}

