package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.util.Duration;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

/**
 * Classe de contrôleur pour la vue principale de l'application.
 * Gère la navigation principale et les transitions entre différentes vues.
 */
public class MainController implements Initializable {

    @FXML
    private Button btn_seconnecter;

    @FXML
    private Button btn_sinscrire;

    @FXML
    private VBox VBox;
    
    private Parent fxml;
    
    /**
     * Ouvre la vue de connexion avec une transition fluide.
     */
    @FXML
    void openSignIn() {
        // Création de la transition de translation
        TranslateTransition t = new TranslateTransition(Duration.seconds(1), VBox);
        // Déplacement vers la position X correspondant à 7 fois la position actuelle de VBox
        t.setToX(VBox.getLayoutX() * 7);
        // Démarrage de la transition
        t.play();
        // Gestion de l'événement à la fin de la transition
        t.setOnFinished(e -> {
            try {
                // Chargement de la vue SignIn.fxml
                fxml = FXMLLoader.load(getClass().getResource("../interfaces/SignIn.fxml"));
                // Remplacement des enfants de VBox par la nouvelle vue
                VBox.getChildren().removeAll();
                VBox.getChildren().setAll(fxml);
            } catch (Exception el) {
                el.printStackTrace();
            }
        });
    }
    	
    /**
     * Ouvre la vue d'inscription avec une transition fluide.
     */
    @FXML
    void openSignUp() {
        // Création de la transition de translation
        TranslateTransition t = new TranslateTransition(Duration.seconds(1), VBox);
        // Déplacement vers la position X correspondant à 7
        t.setToX(7);
        // Démarrage de la transition
        t.play();
        // Gestion de l'événement à la fin de la transition
        t.setOnFinished(e -> {
            try {
                // Chargement de la vue SignUp.fxml
                fxml = FXMLLoader.load(getClass().getResource("../interfaces/SignUp.fxml"));
                // Remplacement des enfants de VBox par la nouvelle vue
                VBox.getChildren().removeAll();
                VBox.getChildren().setAll(fxml);
            } catch (Exception el) {
                el.printStackTrace();
            }
        });
    }
    
    /**
     * Initialise le contrôleur principal.
     * Configure la transition de translation initiale pour la VBox.
     *
     * @param location  L'URL du fichier FXML à initialiser.
     * @param resources Le ResourceBundle contenant des données spécifiques à la localisation.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Création de la transition de translation initiale
        TranslateTransition t = new TranslateTransition(Duration.seconds(1), VBox);
        // Déplacement vers la position X correspondant à 7 fois la position actuelle de VBox
        t.setToX(VBox.getLayoutX() * 7);
        // Démarrage de la transition initiale
        t.play();
    }
}
