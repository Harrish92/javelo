package ch.epfl.javelo.gui;


import ch.epfl.javelo.data.Graph;
import ch.epfl.javelo.routing.CityBikeCF;
import ch.epfl.javelo.routing.ElevationProfile;
import ch.epfl.javelo.routing.GpxGenerator;
import ch.epfl.javelo.routing.RouteComputer;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

/**
 * Programme principale de l'application javelo.
 *
 * @author Yoan Giovannini (303934)
 */
public final class JaVelo extends Application {

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Graph graph = Graph.loadFrom(Path.of("javelo-data"));
        Path cacheBasePath = Path.of("osm-cache");
        String tileServerHost = "tile.openstreetmap.org";
        TileManager tileManager =
                new TileManager(cacheBasePath, tileServerHost);
        BorderPane mainPane = new BorderPane();
        ObservableList<Waypoint> waypoints = FXCollections.observableArrayList();
        ReadOnlyDoubleProperty hp = new SimpleDoubleProperty(1000);
        RouteBean routeBean = new RouteBean(new RouteComputer(graph, new CityBikeCF(graph)));
        routeBean.setPointsList(waypoints);
        ErrorManager errorManager = new ErrorManager();
        AnnotatedMapManager annotatedMapManager = new AnnotatedMapManager(graph,tileManager, routeBean, errorManager :: displayError);
        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(Orientation.VERTICAL);
        splitPane.getItems().add(annotatedMapManager.pane());
        //splitPane.getItems().addAll(annotatedMapManager.pane(), errorManager.pane());
        StackPane stackPane = new StackPane(splitPane, errorManager.pane());
        mainPane.setCenter(stackPane);

        ElevationProfileManager elevationProfileManager = new ElevationProfileManager(
                routeBean.elevationProfile(),routeBean.highlightedPositionProperty());
        SplitPane.setResizableWithParent(elevationProfileManager.pane(), false);


        //barre de menu
        MenuItem menuItem = new MenuItem("Exporter GPX");
        Menu menu = new Menu("Fichier");
        MenuBar menuBar = new MenuBar();
        menu.getItems().add(menuItem);
        menuBar.getMenus().add(menu);
        mainPane.setTop(menuBar);
        menuItem.setDisable(true);
        menuItem.setOnAction(a -> {
            try {
                GpxGenerator.writeGpx("javelo.gpx",
                                        routeBean.getRouteProperty().get(),
                                        routeBean.elevationProfile().get());
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });


        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("JaVelo");
        primaryStage.show();


        //gère l'affichage du profile
        routeBean.elevationProfile().addListener((p, prevS, newS) ->{
            if(newS != null){
                menuItem.setDisable(false);
                if(splitPane.getItems().size() < 2) {
                    splitPane.getItems().add(elevationProfileManager.pane());
                }
            }
            else{
                menuItem.setDisable(true);
            }
            if(/*(prevS == null && newS != null) ||*/ (prevS != null && newS == null)){
                splitPane.getItems().remove(1);
            }
        });

        //gère l'affiche de la highlighted position (marche pas)
        routeBean.highlightedPositionProperty().bind(Bindings.createDoubleBinding(() ->{
                //System.out.println(annotatedMapManager.mousePositionOnRouteProperty().get());
                //System.out.println(elevationProfileManager.mousePositionOnProfileProperty().get());
                //System.out.println(annotatedMapManager.mousePositionOnRouteProperty().get());
                return Double.valueOf(annotatedMapManager.mousePositionOnRouteProperty().get() >= 0 ?
                        annotatedMapManager.mousePositionOnRouteProperty().get() :
                        elevationProfileManager.mousePositionOnProfileProperty().get());
                //return Double.valueOf(elevationProfileManager.mousePositionOnProfileProperty().get());

        }, annotatedMapManager.mousePositionOnRouteProperty(),elevationProfileManager.mousePositionOnProfileProperty()));


    }
}
