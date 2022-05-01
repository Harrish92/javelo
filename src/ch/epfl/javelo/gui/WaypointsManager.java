package ch.epfl.javelo.gui;

import ch.epfl.javelo.data.Graph;
import ch.epfl.javelo.projection.PointCh;
import ch.epfl.javelo.projection.PointWebMercator;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
        Canvas canvas = new Canvas();
        pane = new Pane();
        drawAllPoint();
    }

    /**
     * Gère les actions de l'utilisateur avec les points de passage.
     */
    /*private void canvasEventListener(){
        canvas.setOnDragEntered(e -> {
            double x = e.getX();
            double y = e.getY();
            boolean occupe = false;
            for(Node n : canvas.getChildren()){//TODO: optimiser lambda
                occupe = n.contains(x,y);
            }
            if(occupe){//TODO:calculer le point
                Group group = createGraphicPoint(x,y);
                canvas.getChildren().add(group);
            }
            else{
                addWaypoint(x,y);
            }
        });
        pane.setOnMouseClicked(e -> {
            pane.setPickOnBounds(false);
            double x = e.getX();
            double y = e.getY();
            boolean occupe = false;
            for(Node n : pane.getChildren()){//TODO: optimiser lambda
                occupe = n.contains(x,y);
            }
            //canvas.getChildren().forEach((n) -> occupe = n.contains(x,y));
            if(!occupe){
                addWaypoint(x,y);
                pane.getChildren().add(createGraphicPoint(x,y));
            }
    });
    }*/

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
        svgPoint.getStyleClass().add("pin_inside");
        svgCircle.getStyleClass().add("pin_outside");
        group.getChildren().addAll(svgPoint, svgCircle);
        group.setLayoutX(x);
        group.setLayoutY(y);
        System.out.println(x+"JJJ"+y);
        return group;
    }


    private void pointEventListener(Group group, int pointIndex){
        ObjectProperty<Point2D> position = new SimpleObjectProperty<Point2D>();
        group.setOnMouseClicked(e -> {
            pane.setPickOnBounds(false);
            pointsListe.remove(pointsListe.get(pointIndex));
            pane.getChildren().remove(group);
        });

        group.setOnMousePressed(e -> {
            position.set(new Point2D(e.getX(), e.getY()));
        });

        group.setOnMouseDragged(e -> {
            double x = e.getX();
            double y= e.getY();
            group.setLayoutX(x);
            group.setLayoutY(y);
            position.set(new Point2D(x, y));//UTILE ?
        });

        group.setOnMouseReleased(e -> {
            changeWaypoint(position.get().getX(), position.get().getY(),pointIndex);

        });


        /*group.setOnMouseDragEntered(e -> {
            pane.setPickOnBounds(false);
            System.out.println("DRAG ENTERED");

        });
        group.setOnMouseDragOver(e -> {
            pane.setPickOnBounds(false);
            group.setLayoutX(e.getX());
            group.setLayoutY(e.getY());
            //e.acceptTransferModes(TransferMode.MOVE);
            e.consume();
            System.out.println("DRAG OVER");
        });*/
    }

    /**
     * Recréé tous les points graphiques.
     */
    private void drawAllPoint(){
        pane.getChildren().removeAll();
        for (int k = 0; k < pointsListe.size(); k++) {
            Waypoint point = pointsListe.get(k);
            PointWebMercator pm = PointWebMercator.ofPointCh(point.PointCH());
            double x = property.get().viewX(pm);
            double y = property.get().viewY(pm);
            Group group = createGraphicPoint(x,y);
            String s = "";
            if(k == 0) s = "first";
            else if(k == pointsListe.size()) s = "last";
            else s = "middle";
            group.getStyleClass().add(s);
            //pointEventListener(group, k);
            pane.getChildren().add(group);
            System.out.println(k);
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
            Waypoint waypoint = new Waypoint(point, nodeId);
            pointsListe.add(waypoint);
            drawAllPoint();
        }
    }

    private void changeWaypoint(double x, double y, int index){
        PointCh point = property.get().pointAt(x,y).toPointCh();
        int nodeId = graph.nodeClosestTo(point, SEARCHDISTANCE);
        if(nodeId == -1){
            erreurs.accept("Aucune route à proximité !");
        }
        else{
            Waypoint waypoint = new Waypoint(point, nodeId);
            pointsListe.remove(index);
            pointsListe.add(index,waypoint);
            drawAllPoint();
        }
    }
}
