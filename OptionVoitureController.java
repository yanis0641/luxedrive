package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

/**
 * Contrôleur pour la vue des options liées aux voitures.
 * Gère les actions liées à l'ajout, la suppression et la recherche de voitures.
 */
public class OptionVoitureController {
    
    @FXML
    private Parent fxml;

    @FXML
    private AnchorPane root;

    @FXML
    private Button addButton;

    @FXML
    private Button rechercheBoutton;

    @FXML
    private Button deleteButton;

    /**
     * Charge la vue d'ajout de voiture en réponse à l'action de l'utilisateur.
     *
     * @param event L'événement de clic sur le bouton d'ajout de voiture.
     */
    @FXML
    void addVoiture(ActionEvent event) {
        try {
            fxml = FXMLLoader.load(getClass().getResource("/interfaces/Voiture.fxml"));
            root.getChildren().removeAll();
            root.getChildren().setAll(fxml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Charge la vue de suppression de voiture en réponse à l'action de l'utilisateur.
     *
     * @param event L'événement de clic sur le bouton de suppression de voiture.
     */
    @FXML
    void deleteVoiture(ActionEvent event) {
        try {
            fxml = FXMLLoader.load(getClass().getResource("/interfaces/suppression.fxml"));
            root.getChildren().removeAll();
            root.getChildren().setAll(fxml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Charge la vue de recherche de voiture en réponse à l'action de l'utilisateur.
     *
     * @param event L'événement de clic sur le bouton de recherche de voiture.
     */
    @FXML
    void rechercheVoiture(ActionEvent event) {
        try {
            fxml = FXMLLoader.load(getClass().getResource("/interfaces/RechercheClient.fxml"));
            root.getChildren().removeAll();
            root.getChildren().setAll(fxml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Charge la vue de recherche de voiture pour les clients en réponse à l'action de l'utilisateur.
     *
     * @param event L'événement de clic sur le bouton de recherche de voiture pour les clients.
     */
    @FXML
    void rechercheVoitureC(ActionEvent event) {
        try {
            fxml = FXMLLoader.load(getClass().getResource("/interfaces/RechercheClientC.fxml"));
            root.getChildren().removeAll();
            root.getChildren().setAll(fxml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Charge un fichier FXML spécifié en paramètre dans la vue principale.
     *
     * @param fxmlPath Le chemin vers le fichier FXML à charger.
     */
    private void loadFXML(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent loadedFXML = loader.load();
            root.getChildren().setAll(loadedFXML);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
