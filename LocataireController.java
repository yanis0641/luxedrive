package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;



/**
 * Contrôleur pour la gestion des opérations liées aux locataires.
 * Permet d'ajouter, supprimer et rechercher des informations sur les locataires.
 */
public class LocataireController {

    @FXML
    private Parent fxml;

    @FXML
    private AnchorPane root;

    @FXML
    private Button addbutton;

    @FXML
    private Button deletebutton;

    @FXML
    private Button rechercheButton;

    /**
     * Gère l'événement du bouton d'ajout de locataire.
     * Charge l'interface graphique pour ajouter un locataire.
     *
     * @param event Événement déclenché par le clic sur le bouton d'ajout.
     */
    @FXML
    void addClient(ActionEvent event) {
        try {
            fxml = FXMLLoader.load(getClass().getResource("/interfaces/addAdmin.fxml"));
            root.getChildren().removeAll();
            root.getChildren().setAll(fxml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gère l'événement du bouton de suppression de locataire.
     * Charge l'interface graphique pour supprimer un locataire.
     *
     * @param event Événement déclenché par le clic sur le bouton de suppression.
     */
    @FXML
    void deleteClient(ActionEvent event) {
        try {
            fxml = FXMLLoader.load(getClass().getResource("/interfaces/DeleteAdmin.fxml"));
            root.getChildren().removeAll();
            root.getChildren().setAll(fxml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gère l'événement du bouton de recherche de locataire.
     * Charge l'interface graphique pour rechercher un locataire.
     *
     * @param event Événement déclenché par le clic sur le bouton de recherche.
     */
    @FXML
    void rechercheClient(ActionEvent event) {
        try {
            fxml = FXMLLoader.load(getClass().getResource("/interfaces/RechercheAdmin.fxml"));
            root.getChildren().removeAll();
            root.getChildren().setAll(fxml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Charge un fichier FXML spécifié et le défini comme nouveau nœud racine.
     *
     * @param fxmlPath Chemin vers le fichier FXML à charger.
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
