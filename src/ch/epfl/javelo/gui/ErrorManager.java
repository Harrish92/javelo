package ch.epfl.javelo.gui;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Gère l'affichage de messages d'erreur.
 *
 * @author Harrishan Raveendran (345291)
 */


public final class ErrorManager {
    private final VBox vBox;
    private final Text errorMessage;
    private SequentialTransition sequentialTransition;
    private final static double FADEIN_DURATION_SECONDS = 0.2;
    private final static double FADEOUT_DURATION_SECONDS = 0.5;
    private final static double PAUSE_DURATION_SECONDS = 2;
    private final static double OPACITY_OFF = 0;
    private final static double OPACITY_ON = 0.8;

    public ErrorManager(){
        this.errorMessage = new Text();
        this.vBox = new VBox();
        vBox.getStyleClass().add("error.css");
        vBox.setDisable(true);
        addAnimation();

    }

    /** ajout des transitions pour le panneau*/
    private void addAnimation(){
        FadeTransition fadeInTransition = new FadeTransition(Duration.seconds(FADEIN_DURATION_SECONDS));
        fadeInTransition.setFromValue(OPACITY_OFF);
        fadeInTransition.setToValue(OPACITY_ON);
        FadeTransition fadeOutTransition1 = new FadeTransition(Duration.seconds(FADEOUT_DURATION_SECONDS));
        fadeOutTransition1.setFromValue(OPACITY_ON);
        fadeOutTransition1.setToValue(OPACITY_OFF);
        sequentialTransition = new SequentialTransition(
                vBox,
                fadeInTransition,
                new PauseTransition(Duration.seconds(PAUSE_DURATION_SECONDS)),
                fadeOutTransition1
        );
    }

    /** retourne le panneau contenant le message d'erreur*/
    public Pane pane(){
        return vBox;
    }

    /**
     * affiche le message d'erreur à l'écran en émettant un son
     *
     * @param message le message d'erreur représenté par une chaîne de caractères
     */
    public void displayError(String message){
        vBox.getChildren().removeAll(vBox.getChildren());
        sequentialTransition.stop();
        errorMessage.setText(message);
        vBox.getChildren().add(errorMessage);

        java.awt.Toolkit.getDefaultToolkit().beep();
        sequentialTransition.play();
    }
}
