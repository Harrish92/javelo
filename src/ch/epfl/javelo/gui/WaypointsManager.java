package ch.epfl.javelo.gui;

import ch.epfl.javelo.data.Graph;
import ch.epfl.javelo.data.GraphNodes;
import ch.epfl.javelo.data.GraphSectors;
import ch.epfl.javelo.projection.Ch1903;
import ch.epfl.javelo.projection.PointCh;
import ch.epfl.javelo.projection.PointWebMercator;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;

import java.util.function.Consumer;

/**
 * Gère l'affichage et l'interaction avec les points de passage.
 * @author Yoan Giovannini (303934)
 */
public final class WaypointsManager {
    private final int SEARCHDISTANCE = 1000;
    private Graph graph;
    private ObjectProperty<MapViewParameters> property;
    private ObservableList<Waypoint> pointsListe;
    private Consumer<String> erreurs;
    private Pane pane;
    private Point2D mousePoint;
    private Point2D mouse_difference;


    /**
     * Constructeur de WaypointsManager.
     * @param graph le graphe su réseau routier.
     * @param property les paramètres de la carte affichée.
     * @param pointsListe la liste observable de tous les points de passage.
     * @param erreurs un objet permettant de signaler les erreurs.
     */
    public WaypointsManager(Graph graph, ObjectProperty<MapViewParameters> property,
                            ObservableList<Waypoint> pointsListe, Consumer<String> erreurs){
        this.graph = graph;
        this.property = property;
        this.pointsListe = pointsListe;
        this.erreurs = erreurs;
        pane = new Pane();
        drawAllPoint();
        paneEventListener();
    }

    private void paneEventListener(){
        pane.setPickOnBounds(false);
        property.addListener(e -> drawAllPoint());
        pointsListe.addListener((ListChangeListener<? super Waypoint>) l -> drawAllPoint());

    }

    /**
     * Créé un groupe javaFX avec le dessin d'un point et lui donne une position.
     * @param x la coordonnée x de la position du point.
     * @param y la coordonnée y de la position du point.
     * @return un groupe javaFX.
     */
    private Group createGraphicPoint(double x, double y){
        Group group = new Group();
        SVGPath svgPoint = new SVGPath();
        SVGPath svgCircle = new SVGPath();
        svgPoint.setContent("M-8-20C-5-14-2-7 0 0 2-7 5-14 8-20 20-40-20-40-8-20");
        svgCircle.setContent("M0-23A1 1 0 000-29 1 1 0 000-23");
        svgPoint.getStyleClass().add("pin_outside");
        svgCircle.getStyleClass().add("pin_inside");
        group.getChildren().addAll(svgPoint, svgCircle);
        group.setLayoutX(x);
        group.setLayoutY(y);
        return group;
    }


    private void pointEventListener(Group group, int pointIndex){
        ObjectProperty<Point2D> initPosition = new SimpleObjectProperty<Point2D>();
        group.setOnMouseClicked(e -> {

        });

        group.setOnMousePressed(e -> {
            mousePoint = new Point2D(e.getSceneX(), e.getSceneY());
            initPosition.set(new Point2D(group.getLayoutX(), group.getLayoutY()));
        });

        group.setOnMouseDragged(e -> {
            mouse_difference = mousePoint.subtract(e.getSceneX(), e.getSceneY());
            PointWebMercator pointM = property.get().pointAt(
                    initPosition.get().getX() - mouse_difference.getX(),
                    initPosition.get().getY() - mouse_difference.getY());
            group.setLayoutX(property.get().viewX(pointM));
            group.setLayoutY(property.get().viewY(pointM));
        });

        group.setOnMouseReleased(e -> {
            if(e.isStillSincePress()) {
                pane.getChildren().remove(pointIndex);
                pointsListe.remove(pointsListe.get(pointIndex));
            }
            else{
                double x = initPosition.get().getX() - mouse_difference.getX();
                double y = initPosition.get().getY() - mouse_difference.getY();
                PointCh point = property.get().pointAt(x,y).toPointCh();
                int nodeId = graph.nodeClosestTo(point, SEARCHDISTANCE);
                if(nodeId == -1){
                    erreurs.accept("Aucune route à proximité !");
                    group.setLayoutX(initPosition.get().getX());
                    group.setLayoutY(initPosition.get().getY());
                }

                else{
                    Waypoint waypoint = new Waypoint(graph.nodePoint(nodeId), nodeId);
                    pointsListe.remove(pointIndex);
                    pointsListe.add(pointIndex,waypoint);
                }
        }});
    }

    /**
     * Recréé tous les points graphiques.
     */
    private void drawAllPoint(){
        pane.getChildren().removeAll(pane.getChildren());
        for (int k = 0; k < pointsListe.size(); k++) {
            Waypoint point = pointsListe.get(k);
            PointWebMercator pm = PointWebMercator.ofPointCh(point.PointCH());
            double x = property.get().viewX(pm);
            double y = property.get().viewY(pm);
            Group group = createGraphicPoint(x,y);
            String s = "";
            if(k == 0) s = "first";
            else if(k == pointsListe.size() - 1) s = "last";
            else s = "middle";
            group.getStyleClass().addAll("pin",s);
            pointEventListener(group, k);
            pane.getChildren().add(group);
        }
    }

    /**
     * Retourne le panneau contenant les points de passage.
     * @return un panneau javaFX.
     */
    public Pane pane(){
        return pane;
    }

    /**
     * Ajoute un nouveau point de passage au noeud du graph
     * qui en est le plus proche.
     * @param x la coordonnée x du point.
     * @param y la coordonnée y du point.
     */
    public void addWaypoint(double x, double y){
        PointCh point = property.get().pointAt(x,y).toPointCh();
        int nodeId = graph.nodeClosestTo(point, SEARCHDISTANCE);
        if(nodeId == -1){
            erreurs.accept("Aucune route à proximité !");
        }
        else{
            Waypoint waypoint = new Waypoint(graph.nodePoint(nodeId), nodeId);
            pointsListe.add(waypoint);
        }
    }

    private void changeWaypoint(double x, double y, int index){
        PointCh point = property.get().pointAt(x,y).toPointCh();
        int nodeId = graph.nodeClosestTo(point, SEARCHDISTANCE);
        if(nodeId == -1){
            erreurs.accept("Aucune route à proximité !");
        }
        else{
            Waypoint waypoint = new Waypoint(graph.nodePoint(nodeId), nodeId);
            pointsListe.remove(index);
            pointsListe.add(index,waypoint);
        }
    }
}
