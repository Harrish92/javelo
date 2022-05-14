package ch.epfl.javelo.gui;

import ch.epfl.javelo.Math2;
import ch.epfl.javelo.projection.PointWebMercator;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 *
 * BaseMapManager représente une gestionnaire de carte
 *
 * @author Harrishan Raveendran (345291)
 *
 */
public final class BaseMapManager {
    private final TileManager tm;
    private final ObjectProperty<MapViewParameters> property;
    private boolean redrawNeeded;
    private final Pane pane;
    private final Canvas canvas;
    private ObjectProperty<Point2D> mousePoint;
    private final static int TILE_LENGTH= 256;
    private final static int MIN_ZOOM = 8;
    private final static int MAX_ZOOM = 19;



    //TODO faire les commentaires
    public BaseMapManager(TileManager tm,WaypointsManager wpm , ObjectProperty<MapViewParameters> property){
        this.tm = tm;
        this.property = property;

        canvas = new Canvas();
        pane = new Pane(canvas);
        canvas.widthProperty().bind(pane.widthProperty());
        canvas.heightProperty().bind(pane.heightProperty());


        canvas.heightProperty().addListener(o -> redrawOnNextPulse());
        canvas.widthProperty().addListener(o -> redrawOnNextPulse());

        pane.setOnMousePressed(e->{
            Point2D point2D = new Point2D(e.getX(), e.getY());
            mousePoint = new SimpleObjectProperty<>(point2D);
        });

        pane.setOnMouseDragged(e ->{
            ObjectProperty<Point2D> mouse_difference = new SimpleObjectProperty<>
                    (mousePoint.get().subtract(e.getX(), e.getY()));
            Point2D topLeft = mouse_difference
                    .get()
                    .add(property.get().topLeft().getX(), property.get().topLeft().getY());


            property.set(new MapViewParameters(property.get().zoomLevel(), topLeft.getX(), topLeft.getY()));
            redrawOnNextPulse();

            mousePoint.set(new Point2D(e.getX(), e.getY()));
        });

        pane.setOnMouseReleased(e->{
            mousePoint = null;
        });

        pane.setOnMouseClicked(e->{
            if(e.isStillSincePress()) {
                wpm.addWaypoint(e.getX(), e.getY());
            }
            redrawOnNextPulse();
        });

        SimpleLongProperty minScrollTime = new SimpleLongProperty();
        pane.setOnScroll(e ->{

            long currentTime = System.currentTimeMillis();
            if (currentTime < minScrollTime.get()) return;
            minScrollTime.set(currentTime + 250);
            double zoomDelta = Math.signum(e.getDeltaY());
            int zoomLevel =  (int) Math.rint(zoomDelta + property.get().zoomLevel());

            zoomLevel = Math2.clamp(MIN_ZOOM, zoomLevel, MAX_ZOOM);

            ObjectProperty<Point2D> mouseOnScrollCoord = new SimpleObjectProperty<>(new Point2D(e.getX(), e.getY()));

            mouseOnScrollCoord.set(mouseOnScrollCoord.get().add(property.get().coordX(), property.get().coordY()));
            double scale = Math.scalb(1, zoomLevel - property.get().zoomLevel());
            mouseOnScrollCoord.set(mouseOnScrollCoord.get().multiply(scale).subtract(e.getX(), e.getY()));
            property.set(new MapViewParameters(zoomLevel, mouseOnScrollCoord.get().getX(),
                    mouseOnScrollCoord.get().getY()));
            redrawOnNextPulse();
        });


        redrawOnNextPulse();

        canvas.sceneProperty().addListener((p, oldS, newS) -> {
            assert oldS == null;
            newS.addPreLayoutPulseListener(this::redrawIfNeeded);
        });

    }

    /**
     *
     * @return un panneau
     */
    public Pane pane(){

        return this.pane;

    }

    /**
     * dessine les tuiles sur la carte
     */
    private void draw(){
        try {
            GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
            int zoom = property.get().zoomLevel();
            double topLeftX = property.get().topLeft().getX();
            double topLeftY = property.get().topLeft().getY();

            int  x0 = (int) topLeftX % TILE_LENGTH;
            int y0 = (int) topLeftY % TILE_LENGTH;

            int tuile0Y = (int) Math.floor(topLeftY / TILE_LENGTH);

            for(int y = -y0; y <= canvas.getHeight(); y+= TILE_LENGTH){
                int tuile0X = (int) Math.floor(topLeftX / TILE_LENGTH);
                for(int x = -x0; x <= canvas.getWidth(); x+= TILE_LENGTH){
                    TileManager.TileId tileId = new TileManager.TileId(zoom, tuile0X, tuile0Y);
                    graphicsContext.drawImage(tm.imageForTileAt(tileId), x, y);
                    tuile0X += 1;
                }
                tuile0Y += 1;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * redessine les tuiles sur la carte si nécessaire
     */
    private void redrawIfNeeded(){
        if (!redrawNeeded) return;
        redrawNeeded = false;

        // dessin de la carte
        draw();

    }

    /**
     * redessine au prochain battement
     */
    private void redrawOnNextPulse() {
        redrawNeeded = true;
        Platform.requestNextPulse();
    }

}

