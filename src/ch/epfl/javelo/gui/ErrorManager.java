package ch.epfl.javelo.gui;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public final class ErrorManager {
    private Pane pane;

    public ErrorManager(){
        VBox vBox = new VBox();
        this.pane = new Pane();
        init();

    }

    private void init(){
        VBox vBox = new VBox();
    }

    public Pane pane(){
        return pane;
    }

    public void displayError(String message){

    }
}
