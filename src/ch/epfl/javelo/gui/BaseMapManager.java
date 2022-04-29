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
import java.util.ArrayList;
import java.util.Map;

public final class BaseMapManager {
    private final TileManager tm;
    private final ObjectProperty<MapViewParameters> property;
    private boolean redrawNeeded;
    private final static int MAP_LENGTH = 256;


    public BaseMapManager(TileManager tm, ObjectProperty<MapViewParameters> property){
        redrawNeeded = false;
        this.tm = tm;
        this.property = property;
        int nbOfTiles;
        Pane pane = pane();

        Canvas canvas = new Canvas();
        pane.getChildren().add(canvas);
        canvas.widthProperty().bind(pane.widthProperty());
        GraphicsContext graphicsContext =  canvas.getGraphicsContext2D();

        int zoom = property.get().zoomLevel();
        int topLeftX = (int) Math.floor(0);property.get().topLeft().getX();
        int topLeftY = (int) Math.floor(0);property.get().topLeft().getY();
        int bottomRigthX = (int) Math.floor(topLeftX + canvas.getWidth() );
        int bottomRigthY = (int) Math.floor(topLeftY  + canvas.getHeight());


        nbOfTiles = (zoom != 0)
                ? (int) Math.pow(2, zoom)
                : 1;

        int xTile =(int) Math.floor(property.get().coordX() / MAP_LENGTH);
        int yTile =(int) Math.floor(property.get().coordY() / MAP_LENGTH);
        for(int y = topLeftY; y <= bottomRigthY; y+= MAP_LENGTH / nbOfTiles){ //remplace MAP_LENGTH par CanvasWidth
            for(int x = topLeftX; x <= bottomRigthX; x += MAP_LENGTH / nbOfTiles){
                TileManager.TileId tileId = new TileManager.TileId(zoom, xTile, yTile);
                xTile += (MAP_LENGTH / nbOfTiles) / MAP_LENGTH;
            }
            yTile +=  (MAP_LENGTH / nbOfTiles) / MAP_LENGTH;

        }







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

