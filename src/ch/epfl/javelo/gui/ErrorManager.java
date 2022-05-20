package ch.epfl.javelo.gui;

import javafx.scene.layout.Pane;

public final class ErrorManager {
    private Pane pane;

    public ErrorManager(){
        this.pane = new Pane();

    }

    public Pane pane(){
        return pane;
    }

    public void displayError(String message){

    }
}
