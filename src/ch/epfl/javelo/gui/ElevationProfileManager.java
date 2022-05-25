package ch.epfl.javelo.gui;

import ch.epfl.javelo.routing.ElevationProfile;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
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
 * @author Harrishan Raveendran (345291)
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
    private final DoubleProperty mousePositionOnProfile;
    private final Line position;
    private final Group groupForTextInRectangle;
    private final Text stats;





    public ElevationProfileManager(ReadOnlyObjectProperty<ElevationProfile> elevationProfileProperty,
                                   ReadOnlyDoubleProperty highlightedPosition){
        this.elevationProfileProperty = elevationProfileProperty;
        this.highlightedPosition = highlightedPosition;
        borderPane = new BorderPane();
        pane = new Pane();
        profile = new Polygon();
        mousePositionOnProfile = new SimpleDoubleProperty(Double.NaN);
        insets = new Insets(10, 10, 20, 40);
        screenToWorld = new SimpleObjectProperty<>(new Affine());
        worldToScreen = new SimpleObjectProperty<>(new Affine());
        rectangle = new SimpleObjectProperty<Rectangle2D>();
        position = new Line();
        groupForTextInRectangle = new Group();
        stats = new Text();
        initLayout();
        pane.getChildren().add(groupForTextInRectangle);
        events();
    }

    /**
     * Créé les éléments javaFX de base pour l'affichage du profil.
     */
    private void initLayout(){
        VBox profileData = new VBox();
        Path grid = new Path();
        Group label = new Group();
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

    /**
     * Dessine le profile.
     */
    private void draw(){
        try{
            if(elevationProfileProperty.get() != null) {
                initTransformation();
                initGrid();
                stats.setText(getStats());
            }
        }
        catch (NonInvertibleTransformException ex){
            System.out.println(ex.getStackTrace());
        }
    }

    /**
     * Gère les évènements //TODO:améliorer commentaire
     */
    private void events(){
        rectangle.bind(Bindings.createObjectBinding(() ->
            new Rectangle2D(insets.getLeft(), insets.getBottom(),
                    Math.max(pane.getWidth() - insets.getLeft() -insets.getRight(), 0),
                    Math.max(pane.getHeight() - insets.getBottom() - insets.getTop(), 0))
        , pane.widthProperty(), pane.heightProperty()));

        rectangle.addListener((p, oldS, newS) -> draw());
        elevationProfileProperty.addListener((p, oldS, newS) -> draw());
        worldToScreen.addListener((p, oldS, newS) ->{
                if(elevationProfileProperty.get() != null) initProfile();});
        pane.setOnMouseMoved(e -> {
            if(rectangle.get().contains(e.getX(), e.getY())){
                mousePositionOnProfile.set(Math.round(screenToWorld.get().transform(e.getSceneX(),0).getX()));
            }
            else{
                mousePositionOnProfile.set(Double.NaN);
            }
        });
        pane.setOnMouseExited(e -> mousePositionOnProfile.set(Double.NaN));

        position.layoutXProperty().bind(Bindings.createDoubleBinding(() ->
                worldToScreen.get().transform(highlightedPosition.get(),0).getX(), highlightedPosition, worldToScreen));
        position.startYProperty().bind(Bindings.select(rectangle, "minY"));
        position.endYProperty().bind(Bindings.select(rectangle, "maxY"));
        position.visibleProperty().bind(highlightedPosition.greaterThanOrEqualTo(0));
    }


    /**
     * Transformation du profil su l'écran au profil réel et inversement.
     * @throws NonInvertibleTransformException si la transformation n'est pas inversible.
     */
    private void initTransformation() throws NonInvertibleTransformException {
        Affine transMethods = new Affine();
        transMethods.prependTranslation(-insets.getLeft(), -insets.getBottom());
        transMethods.prependScale(elevationProfileProperty.get().length() / rectangle.get().getWidth(),
                    -(elevationProfileProperty.get().maxElevation() - elevationProfileProperty.get().minElevation()) / rectangle.get().getHeight());
        transMethods.prependTranslation(0, elevationProfileProperty.get().maxElevation());
        screenToWorld.set(transMethods);
        worldToScreen.set(screenToWorld.get().createInverse());
    }

    /**
     * Créé le profile affiché à l'écran.
     */
    private void initProfile(){
        profile.getPoints().clear();
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

    /**
     * Créé la grille du profil adaptée à la taille de la fenêtre.
     */
    private void initGrid(){
        int xStepOnWorld = POS_STEPS[POS_STEPS.length-1];
        int yStepOnWorld = ELE_STEPS[ELE_STEPS.length-1];
        groupForTextInRectangle.getChildren().removeAll(groupForTextInRectangle.getChildren());
        Path grid = (Path) pane.getChildren().get(0);
        grid.getElements().removeAll(grid.getElements());
        ElevationProfile ep = elevationProfileProperty.get();

        for (int pos_step : POS_STEPS) {
            if (worldToScreen.get().deltaTransform(pos_step, 0).getX() >= MIN_VERTICAL_DISTANCE){
                xStepOnWorld = pos_step;
                break;
            }
        }

        for(int ele_step : ELE_STEPS){
            if (worldToScreen.get().deltaTransform(0, -ele_step).getY() >= MIN_HORIZONTAL_DISTANCE) {
                yStepOnWorld = ele_step;
                break;
            }
        }

        int xStepOnScreen = (int) worldToScreen.get().deltaTransform(xStepOnWorld, 0).getX();
        int yStepOnScreen = (int) worldToScreen.get().deltaTransform(0, yStepOnWorld).getY();

        double firstElevationOnTheGrid = (ep.minElevation() % yStepOnWorld == 0)
                ? ep.minElevation() : ep.minElevation() + yStepOnWorld - (ep.minElevation() % yStepOnWorld);
        int firstHorizontalLine = (int) worldToScreen.get().transform(0, firstElevationOnTheGrid).getY();


        MoveTo moveTo = new MoveTo();
        LineTo lineTo = new LineTo();
        moveTo.setX(rectangle.get().getMinX());
        moveTo.setY(firstHorizontalLine);
        grid.getElements().add(moveTo);
        lineTo.setX(rectangle.get().getMaxX());
        lineTo.setY(firstHorizontalLine);
        grid.getElements().add(lineTo);





        int textValueForElevation = (int) firstElevationOnTheGrid;
        for(int y = firstHorizontalLine; y > rectangle.get().getMinY(); y += yStepOnScreen){

            Text textForElevation = new Text();
            textForElevation.setFont(Font.font("Avenir", 10));
            textForElevation.getStyleClass().addAll("grid_label", "vertical");
            textForElevation.setText(String.format("%d", textValueForElevation));
            textForElevation.textOriginProperty().set(VPos.CENTER);
            textForElevation.setLayoutX(rectangle.get().getMinX() - textForElevation.prefWidth(0) - 2);

            textForElevation.setLayoutY(y);
            groupForTextInRectangle.getChildren().add(textForElevation);

            moveTo = new MoveTo();
            lineTo = new LineTo();
            moveTo.setX(rectangle.get().getMinX());
            moveTo.setY(y);
            grid.getElements().add(moveTo);
            lineTo.setX(rectangle.get().getMaxX());
            lineTo.setY(y);

            textValueForElevation += yStepOnWorld;
            grid.getElements().add(lineTo);

        }

        int textValueForPosition = 0;
        for(int x= (int) rectangle.get().getMinX(); x < rectangle.get().getMaxX(); x += xStepOnScreen){

            Text textForPosition = new Text();
            textForPosition.setText(String.format("%d", textValueForPosition));
            textForPosition.setFont(Font.font("Avenir", 10));
            textForPosition.textOriginProperty().set(VPos.TOP);
            textForPosition.getStyleClass().addAll("grid_label", "horizontal");
            textForPosition.setLayoutX(x - textForPosition.prefWidth(0) / 2);
            textForPosition.setLayoutY(rectangle.get().getMaxY());

            moveTo = new MoveTo();
            lineTo = new LineTo();
            moveTo.setX(x);
            moveTo.setY(rectangle.get().getMinY());
            grid.getElements().add(moveTo);
            lineTo.setX(x);
            lineTo.setY(rectangle.get().getMaxY());

            textValueForPosition += xStepOnWorld / (int) KILOMETER;
            groupForTextInRectangle.getChildren().add(textForPosition);
            grid.getElements().add(lineTo);
        }




    }


    /**
     * Créé une chaine de caractères contenant des informations sur le profile.
     * @return une chaine de caractères.
     */
    private String getStats(){
        ElevationProfile ep = elevationProfileProperty.get();
        if(ep == null) return "";
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
     * Retourne une propriété en lecture seule contenant la position
     * du pointeur de la souris le long du profil (en mètres, arrondie
     * à l'entier le plus proche), ou NaN si le pointeur de la souris ne
     * se trouve pas au-dessus du profil.
     * @return  une propriété contenant un entier en lecture seule.
     */
    public ReadOnlyDoubleProperty mousePositionOnProfileProperty(){
        return mousePositionOnProfile;
    }
}
