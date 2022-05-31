package ch.epfl.javelo.gui;

import ch.epfl.javelo.Math2;
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
    private final ObjectProperty<MapViewParameters> mapViewParametersProperty;
    private final WaypointsManager wpm;
    private boolean redrawNeeded;
    private final Pane pane;
    private final Canvas canvas;
    private ObjectProperty<Point2D> mousePoint;
    private final static int TILE_LENGTH= 256;
    private final static int MIN_ZOOM = 8;
    private final static int MAX_ZOOM = 19;


    /**
     *
     * @param tm le gestionnaire de tuiles
     * @param wpm le gestionnaire de points de passage
     * @param mapViewParametersProperty une propriété JavaFX contenant les paramètres de la carte affichée
     */
    public BaseMapManager(TileManager tm,WaypointsManager wpm , ObjectProperty<MapViewParameters> mapViewParametersProperty){
        this.tm = tm;
        this.wpm = wpm;
        this.mapViewParametersProperty = mapViewParametersProperty;

        canvas = new Canvas();
        pane = new Pane(canvas);
        canvas.widthProperty().bind(pane.widthProperty());
        canvas.heightProperty().bind(pane.heightProperty());


        canvas.heightProperty().addListener(o -> redrawOnNextPulse());
        canvas.widthProperty().addListener(o -> redrawOnNextPulse());

        events();
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
    private void drawTilesOnCanvas(){
        try {
            GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
            int zoom = mapViewParametersProperty.get().zoomLevel();
            double topLeftX = mapViewParametersProperty.get().topLeft().getX();
            double topLeftY = mapViewParametersProperty.get().topLeft().getY();

            int  x0 = (int) topLeftX % TILE_LENGTH;
            int y0 = (int) topLeftY % TILE_LENGTH;

            int tile0Y = (int) Math.floor(topLeftY / TILE_LENGTH);

            for(int y = -y0; y <= canvas.getHeight(); y+= TILE_LENGTH){
                int tile0X = (int) Math.floor(topLeftX / TILE_LENGTH);
                for(int x = -x0; x <= canvas.getWidth(); x+= TILE_LENGTH){
                    TileManager.TileId tileId = new TileManager.TileId(zoom, tile0X, tile0Y);
                    graphicsContext.drawImage(tm.imageForTileAt(tileId), x, y);
                    tile0X += 1;
                }
                tile0Y += 1;
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
        drawTilesOnCanvas();

    }

    /**
     * redessine au prochain battement
     */
    private void redrawOnNextPulse() {
        redrawNeeded = true;
        Platform.requestNextPulse();
    }

    /**
     * gestion des évènements de la souris
     *
     * évènements en question :
     *
     * 1. roulement de la molette de la souris qui change le niveau de zoom
     * 2. déplacement sur la carte qui se fait en déplaçant la souris tout en maintenant le bouton gauche
     * 3. clic avec la souris qui permet d'ajouter un point de passage
     *
     */
    private void events(){
        pane.setOnMousePressed(e->{
            Point2D point2D = new Point2D(e.getX(), e.getY());
            mousePoint = new SimpleObjectProperty<>(point2D);
        });

        pane.setOnMouseDragged(e ->{
            ObjectProperty<Point2D> mouse_difference = new SimpleObjectProperty<>
                    (mousePoint.get().subtract(e.getX(), e.getY()));

            Point2D topLeft = mouse_difference
                    .get()
                    .add(mapViewParametersProperty.get().topLeft().getX(),
                            mapViewParametersProperty.get().topLeft().getY());

            mapViewParametersProperty.set(new MapViewParameters(mapViewParametersProperty.get().zoomLevel(),
                    topLeft.getX(),
                    topLeft.getY()));

            redrawOnNextPulse();

            mousePoint.set(new Point2D(e.getX(), e.getY()));
        });

        pane.setOnMouseReleased(e-> mousePoint = null);

        pane.setOnMouseClicked(e->{
            if(e.isStillSincePress())
                wpm.addWaypoint(e.getX(), e.getY());

            redrawOnNextPulse();
        });

        SimpleLongProperty minScrollTime = new SimpleLongProperty();
        pane.setOnScroll(e ->{

            if (e.getDeltaY() == 0d) return;
            long currentTime = System.currentTimeMillis();
            if (currentTime < minScrollTime.get()) return;
            minScrollTime.set(currentTime + 200);
            int zoomDelta = (int) Math.signum(e.getDeltaY());
            int zoomLevel =  (int) Math.rint(zoomDelta + mapViewParametersProperty.get().zoomLevel());

            zoomLevel = Math2.clamp(MIN_ZOOM, zoomLevel, MAX_ZOOM);

            ObjectProperty<Point2D> mouseOnScrollCoord = new SimpleObjectProperty<>(new Point2D(e.getX(), e.getY()));

            mouseOnScrollCoord.set(mouseOnScrollCoord.get().add(mapViewParametersProperty.get().coordX(),
                    mapViewParametersProperty.get().coordY()));

            double scale = Math.scalb(1, zoomLevel - mapViewParametersProperty.get().zoomLevel());

            mouseOnScrollCoord.set(mouseOnScrollCoord.get().multiply(scale).subtract(e.getX(), e.getY()));

            mapViewParametersProperty.set(new MapViewParameters(zoomLevel, mouseOnScrollCoord.get().getX(),
                    mouseOnScrollCoord.get().getY()));

            redrawOnNextPulse();
        });
    }


}

