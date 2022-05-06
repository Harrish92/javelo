package ch.epfl.javelo.gui;

import ch.epfl.javelo.routing.ElevationProfile;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.layout.Pane;

import java.awt.geom.Point2D;

/**
 * Gère l'affichage et l'interaction avec le profil en long d'un itinéraire.
 *
 * @author Yoan Giovannini (303934)
 */
public final class ElevationProfileManager {
    private final int[] POS_STEPS =
            { 1000, 2000, 5000, 10_000, 25_000, 50_000, 100_000 };
    private final int[] ELE_STEPS =
            { 5, 10, 20, 25, 50, 100, 200, 250, 500, 1_000 };
    private final ReadOnlyObjectProperty<ElevationProfile> elevationProfileProperty;
    private final ReadOnlyDoubleProperty highlightedPosition;
    private final Pane pane;

    public ElevationProfileManager(ReadOnlyObjectProperty<ElevationProfile> elevationProfileProperty,
                                   ReadOnlyDoubleProperty highlightedPosition){
        this.elevationProfileProperty = elevationProfileProperty;
        this.highlightedPosition = highlightedPosition;
        pane = new Pane();

    }

    /**
     * @return le panneau contenant le dessin du profil.
     */
    public Pane pane(){return pane;}

    /**
     *
     * @return une propriété en lecture seule contenant la position
     * du pointeur de la souris le long du profil (en mètres, arrondie
     * à l'entier le plus proche), ou NaN si le pointeur de la souris ne
     * se trouve pas au-dessus du profil.
     */
    public ReadOnlyObjectProperty<Point2D> mousePositionOnProfileProperty(){
        return null;
    }
}
