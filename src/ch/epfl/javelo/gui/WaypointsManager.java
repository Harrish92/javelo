package ch.epfl.javelo.gui;

import ch.epfl.javelo.data.Graph;
import ch.epfl.javelo.projection.PointCh;
import ch.epfl.javelo.projection.PointWebMercator;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;

import java.util.List;
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
    }

    /**
     * Retourne le panneau contenant les points de passage.
     * @return un panneau javaFX.
     */
    public Pane pane(){

        return null;
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
            pointsListe.add(new Waypoint(point, nodeId));
        }
    }
}
