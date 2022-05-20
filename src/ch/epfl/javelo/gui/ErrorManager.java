package ch.epfl.javelo.gui;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

public final class ErrorManager {
    private final Pane pane;
    private final VBox vBox;
    private final Text errorMessage;
    private SequentialTransition sequentialTransition;
    private final static double TRANSITION_IN_SECONDS = 0.2;
    private final static double FROM_VALUE = 0;
    private final static double TO_VALUE = 0.8;

    public ErrorManager(){
        this.pane = new Pane();
        this.vBox = new VBox();
        vBox.getStyleClass().add("error");
        this.errorMessage = new Text();
        vBox.getChildren().add(errorMessage);
        pane.getChildren().add(vBox);

        initAnimation();

    }

    private void initAnimation(){
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(TRANSITION_IN_SECONDS), vBox);
        fadeTransition.setFromValue(FROM_VALUE);
        fadeTransition.setToValue(TO_VALUE);
        this.sequentialTransition = new SequentialTransition(new PauseTransition(Duration.seconds(2)),
                fadeTransition
        );

    }

    public Pane pane(){
        return pane;
    }

    public void displayError(String message){
        sequentialTransition.stop();
        errorMessage.setText(message);
        java.awt.Toolkit.getDefaultToolkit().beep();
        sequentialTransition.play();
    }
}
