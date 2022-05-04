package ch.epfl.javelo.gui;

import ch.epfl.javelo.data.Graph;
import ch.epfl.javelo.projection.PointCh;
import ch.epfl.javelo.projection.PointWebMercator;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.shape.SVGPath;

import java.util.function.Consumer;

/**
 * Gère l'affichage et l'interaction avec les points de passage.
 * @author Yoan Giovannini (303934)
 */
public final class WaypointsManager {
    private final int SEARCHDISTANCE = 500;
    private final Graph graph;
    private final ObjectProperty<MapViewParameters> property;
    private final ObservableList<Waypoint> pointsListe;
    private final Consumer<String> erreurs;
    private final Pane pane;


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

    /**
     * Gère les événements liés au dessin de la carte.
     */
    private void paneEventListener(){
        pane.setPickOnBounds(false);
        property.addListener(e -> positionAllPoint());
        pointsListe.addListener((ListChangeListener<? super Waypoint>) l -> drawAllPoint());

    }

    /**
     * Créé un groupe javaFX avec le dessin d'un point.
     * @return un groupe javaFX.
     */
    private Group createGraphicPoint(){
        Group group = new Group();
        SVGPath svgPoint = new SVGPath();
        SVGPath svgCircle = new SVGPath();
        svgPoint.setContent("M-8-20C-5-14-2-7 0 0 2-7 5-14 8-20 20-40-20-40-8-20");
        svgCircle.setContent("M0-23A1 1 0 000-29 1 1 0 000-23");
        svgPoint.getStyleClass().add("pin_outside");
        svgCircle.getStyleClass().add("pin_inside");
        group.getChildren().addAll(svgPoint, svgCircle);
        return group;
    }

    /**
     * Gère les événements liés aux interactions avec les points de passages.
     * @param group le groupe javaFX du point de passage.
     * @param pointIndex l'indexe du groupe dans la liste de points.
     */
    private void pointEventListener(Group group, int pointIndex){
        ObjectProperty<Point2D> mousePoint = new SimpleObjectProperty<>();
        ObjectProperty<Point2D> mouse_difference = new SimpleObjectProperty<>();

        group.setOnMousePressed(e -> {
            mousePoint.set(new Point2D(e.getSceneX(), e.getSceneY()));
        });

        group.setOnMouseDragged(e -> {
            mouse_difference.set(mousePoint.get().subtract(e.getSceneX(), e.getSceneY()));
            PointWebMercator initPoint = PointWebMercator.ofPointCh(pointsListe.get(pointIndex).PointCH());
            group.setLayoutX(property.get().viewX(initPoint) - mouse_difference.get().getX());
            group.setLayoutY(property.get().viewY(initPoint) - mouse_difference.get().getY());
        });

        group.setOnMouseReleased(e -> {
            if(e.isStillSincePress()) {
                pane.getChildren().remove(pointIndex);
                pointsListe.remove(pointsListe.get(pointIndex));
            }
            else{
                PointWebMercator initPoint = PointWebMercator.ofPointCh(pointsListe.get(pointIndex).PointCH());
                double x = property.get().viewX(initPoint) - mouse_difference.get().getX();
                double y = property.get().viewY(initPoint) - mouse_difference.get().getY();
                PointCh point = property.get().pointAt(x,y).toPointCh();
                int nodeId = graph.nodeClosestTo(point, SEARCHDISTANCE);
                if(nodeId == -1){
                    erreurs.accept("Aucune route à proximité !");
                    group.setLayoutX(property.get().viewX(initPoint));
                    group.setLayoutY(property.get().viewY(initPoint));
                }

                else{
                    Waypoint waypoint = new Waypoint(graph.nodePoint(nodeId), nodeId);
                    pointsListe.remove(pointIndex);
                    pointsListe.add(pointIndex,waypoint);
                }
        }});
    }

    /**
     * Créé tous les points graphiques.
     */
    private void drawAllPoint(){
        pane.getChildren().removeAll(pane.getChildren());
        for (int k = 0; k < pointsListe.size(); k++) {
            Group group = createGraphicPoint();
            String s = "middle";
            if(k == 0) s = "first";
            else if(k == pointsListe.size() - 1) s = "last";
            group.getStyleClass().addAll("pin",s);
            pointEventListener(group, k);
            pane.getChildren().add(group);
        }
        positionAllPoint();
    }

    /**
     * Positionne tout les points sur la carte
     */
    private void positionAllPoint(){
        for (int k = 0; k < pointsListe.size(); k++) {
            Waypoint point = pointsListe.get(k);
            PointWebMercator pm = PointWebMercator.ofPointCh(point.PointCH());
            Group group = (Group) pane.getChildren().get(k);
            group.setLayoutX(property.get().viewX(pm));
            group.setLayoutY(property.get().viewY(pm));
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
}
