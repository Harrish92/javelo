package ch.epfl.javelo.gui;

import ch.epfl.javelo.projection.SwissBounds;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.Pane;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public final class BaseMapManager {
    private final TileManager tm;
    private final ObjectProperty<MapViewParameters> property;
    private boolean redrawNeeded;
    private final static int TILE_LENGTH= 256;


    public BaseMapManager(TileManager tm, ObjectProperty<MapViewParameters> property) throws IOException {
        redrawNeeded = false;
        this.tm = tm;
        this.property = property;

        Pane pane = pane();
        Canvas canvas = new Canvas();
        pane.getChildren().add(canvas);
        canvas.widthProperty().bind(pane.widthProperty());
        GraphicsContext graphicsContext =  canvas.getGraphicsContext2D();


        int zoom = property.get().zoomLevel();
        double offSetX = property.get().topLeft().getX();
        double offSetY = property.get().topLeft().getY();
        int tuile0X = (int) Math.floor(property.get().coordX()/ TILE_LENGTH);
        int tuile0Y = (int) Math.floor(property.get().coordY() / TILE_LENGTH);
        TileManager.TileId tileId = new TileManager.TileId(zoom, tuile0X, tuile0Y);
        graphicsContext.drawImage(tm.imageForTileAt(tileId), 50, 50);


        /*
        for(int y = 0; y <= canvas.getHeight(); y+= TILE_LENGTH){
            for(int x = 0; x <= canvas.getWidth(); x+= TILE_LENGTH){

            }
        }

         */





    }

    public Pane pane(){
        Pane pane = new Pane();
        /*
        pane.setOnScroll(e ->{
            int zooming =  (int) (e.getDeltaY() + 0.5);
            int zoomLevel = property.get().zoomLevel() + zooming;
            property.set(new MapViewParameters(zoomLevel, property.get().coordX(), property.get().coordY()));
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

