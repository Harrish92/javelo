package ch.epfl.javelo.gui;

import ch.epfl.javelo.projection.PointWebMercator;
import javafx.geometry.Point2D;

/**
 * Cette classe représente les paramètres du fond de carte
 * présenté dans l'interface graphique.
 * @author Yoan Giovannini (303934)
 */
public final record MapViewParameters(int zoomLevel, double coordX, double coordY) {


    /**
     * Calcule un point de type Point2D à partir des coordonnées.
     * @return un point sous la forme Point2D.
     */
    public Point2D topLeft() {
        PointWebMercator p = PointWebMercator.of(zoomLevel, coordX, coordY);
        double lon = Math.toDegrees(p.lon());
        double lat = Math.toDegrees(p.lat());
        return new Point2D(lon, lat);
    }

    /**
     * Retourne un objet de type MapViewParameters avec les coordonnées passées
     * en argument et le même niveau d'agrandissement que son récepteur.
     * @param newCoordX la composante x de la coordonnée.
     * @param newCoordY la composante y de la coordonnée.
     * @return une instance de MapViewParameters.
     */
    public MapViewParameters withMinXY(double newCoordX, double newCoordY){
        return new MapViewParameters(zoomLevel, newCoordX, newCoordY);
    }


    /**
     * Calcule un point en coordonnées Mercator à partir des coordonnées x et y
     * relatives par rapport au coin haut gauche de la carte affichée à l'écran.
     * @param relativeX la coordonnée relative x.
     * @param relativeY la coordonnée relative y.
     * @return une instance de PointWebMercator.
     */
    public PointWebMercator pointAt(double relativeX, double relativeY){
        return PointWebMercator.of(zoomLevel, relativeX + coordX, relativeY + coordY);
    }

    //valider les arguments ?
    /**
     * Calcule la coordonnée x relative au coin au gauche de la carte avec un
     * point Mercator donné en argument.
     * @param p le point de type PointWebMercator.
     * @return la coordonnée x.
     */
    public double viewX(PointWebMercator p){
        return p.x() - coordX;
    }

    /**
     * Calcule la coordonnée y relative au coin au gauche de la carte avec un
     * point Mercator donné en argument.
     * @param p le point de type PointWebMercator.
     * @return la coordonnée y.
     */
    public double viewY(PointWebMercator p){
        return p.y() - coordY;
    }

}

