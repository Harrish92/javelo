package ch.epfl.javelo.gui;

import ch.epfl.javelo.routing.ElevationProfile;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.*;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;

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
    private final int MIN_HORIZONTAL_DISTANCE = 25;
    private final int MIN_VERTICAL_DISTANCE = 50;
    private final double KILOMETER = 1000;
    private final int NAN = (int) Double.NaN;
    private final ReadOnlyObjectProperty<ElevationProfile> elevationProfileProperty;
    private final ReadOnlyDoubleProperty highlightedPosition;
    private final BorderPane borderPane;
    private final Pane pane;
    private final Insets insets;
    private final Polygon profile;
    private final ObjectProperty<Rectangle2D> rectangle;
    private final ObjectProperty<Transform> screenToWorld;
    private final ObjectProperty<Transform> worldToScreen;
    private final IntegerProperty mousePositionOnProfile;
    private final Line position;



    public ElevationProfileManager(ReadOnlyObjectProperty<ElevationProfile> elevationProfileProperty,
                                   ReadOnlyDoubleProperty highlightedPosition){
        this.elevationProfileProperty = elevationProfileProperty;
        this.highlightedPosition = highlightedPosition;
        borderPane = new BorderPane();
        pane = new Pane();
        profile = new Polygon();
        mousePositionOnProfile = new SimpleIntegerProperty();
        insets = new Insets(10, 10, 20, 40);
        screenToWorld = new SimpleObjectProperty<>();
        worldToScreen = new SimpleObjectProperty<>();
        rectangle = new SimpleObjectProperty<Rectangle2D>();
        position = new Line();
        initLayout();
        events();
    }

    private void initLayout(){
        VBox profileData = new VBox();
        Path grid = new Path();
        Group label = new Group();
        Text stats = new Text();
        stats.setFont(Font.font("Avenir", 10));
        stats.setText(getStats());
        borderPane.getStylesheets().add("elevation_profile.css");
        profileData.setId("profile_data");
        grid.setId("grid");
        profile.setId("profile");
        pane.getChildren().addAll(grid, label, profile, position);
        profileData.getChildren().add(stats);
        borderPane.setCenter(pane);
        borderPane.setBottom(profileData);

    }

    private void events(){
        rectangle.bind(Bindings.createObjectBinding(() ->
            new Rectangle2D(insets.getLeft(), insets.getBottom(),
                    Math.max(pane.getWidth() - insets.getLeft() -insets.getRight(), 0),
                    Math.max(pane.getHeight() - insets.getBottom() - insets.getTop(), 0))
        , pane.widthProperty(), pane.heightProperty()));

        rectangle.addListener(e ->{
            try{
                initTransformation();
            }
            catch (NonInvertibleTransformException ex){
                System.out.println(ex.getStackTrace());
            }
        });
        //screenToWorld.addListener(e -> initProfile());
        worldToScreen.addListener(e -> initProfile());
        pane.setOnMouseMoved(e -> {
            if(rectangle.get().contains(e.getSceneX(), e.getSceneY())){
                mousePositionOnProfile.set((int) Math.round(e.getSceneX()));
            }
            else{
                mousePositionOnProfile.set(NAN);
            }
        });
        pane.setOnMouseExited(e -> mousePositionOnProfile.set(NAN));

        position.layoutXProperty().bind(Bindings.createDoubleBinding(() ->
                (double) mousePositionOnProfile.get(), mousePositionOnProfileProperty()));
        position.startYProperty().bind(Bindings.select(rectangle, "minY"));
        position.endYProperty().bind(Bindings.select(rectangle, "maxY"));
        position.visibleProperty().bind(mousePositionOnProfile.greaterThanOrEqualTo(0));
    }

    private void initTransformation() throws NonInvertibleTransformException {
        Affine transMethods = new Affine();
        transMethods.prependTranslation(-insets.getLeft(),-insets.getBottom());
        transMethods.prependScale(elevationProfileProperty.get().length()/rectangle.get().getWidth(),
                -(elevationProfileProperty.get().maxElevation() - elevationProfileProperty.get().minElevation())/rectangle.get().getHeight());
        transMethods.prependTranslation(0,elevationProfileProperty.get().maxElevation());
        screenToWorld.set(transMethods);
        worldToScreen.set(screenToWorld.get().createInverse());
    }

    private void initProfile(){
        profile.getPoints().clear();
        //ancienne boucle
        /*for (int j = 0; j < elevationProfileProperty.get().length(); j++) {
            Point2D p = worldToScreen.get().transform(
                    j, elevationProfileProperty.get().elevationAt(j));
            profile.getPoints().addAll(p.getX(),p.getY());
        }*/
        for (int j = (int) rectangle.get().getMinX(); j < rectangle.get().getMaxX(); j++) {
            Point2D p = worldToScreen.get().transform(
                    screenToWorld.get().transform(j,rectangle.get().getMinY()).getX(),
                    elevationProfileProperty.get().elevationAt( screenToWorld.get().transform(j,rectangle.get().getMinY()).getX() ));
            profile.getPoints().addAll(p.getX(),p.getY());
        }
        Point2D pbd = worldToScreen.get().transform(elevationProfileProperty.get().length(),
                elevationProfileProperty.get().minElevation());
        Point2D pbg = worldToScreen.get().transform(0,
                elevationProfileProperty.get().minElevation());
        profile.getPoints().addAll(pbd.getX(), pbd.getY(), pbg.getX(), pbg.getY());
    }

    private void initGrille(){
        Path grid = (Path) pane.getChildren().get(0);
        int nbV = (int) Math.floor(rectangle.get().getHeight()/MIN_VERTICAL_DISTANCE);
        int nbH = (int) Math.floor(rectangle.get().getWidth()/MIN_HORIZONTAL_DISTANCE);

        grid.getElements().add(new MoveTo());
        grid.getElements().add(new LineTo());
    }


    private String getStats(){
        ElevationProfile ep = elevationProfileProperty.get();
        return String.format("Longueur : %.1f km" +
                "     Montée : %.0f m" +
                "     Descente : %.0f m" +
                "     Altitude : de %.0f m à %.0f m",
                ep.length()/KILOMETER,
                    ep.totalAscent(),
                    ep.totalDescent(),
                    ep.minElevation(),
                    ep.maxElevation());
    }




    /**
     * @return le panneau contenant le dessin du profil.
     */
    public Pane pane(){return borderPane;}



    /**
     *
     * @return une propriété en lecture seule contenant la position
     * du pointeur de la souris le long du profil (en mètres, arrondie
     * à l'entier le plus proche), ou NaN si le pointeur de la souris ne
     * se trouve pas au-dessus du profil.
     */
    public ReadOnlyIntegerProperty mousePositionOnProfileProperty(){
        return mousePositionOnProfile;
    }
}
