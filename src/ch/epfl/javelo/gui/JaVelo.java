package ch.epfl.javelo.gui;


import ch.epfl.javelo.data.Graph;
import javafx.application.Application;
import javafx.stage.Stage;

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
        Graph graph = Graph.loadFrom(Path.of("lausanne"));
        //TODO: compléters
    }
}
