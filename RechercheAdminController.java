package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Contrôleur pour la vue de recherche des administrateurs.
 * Gère les opérations liées à la recherche de clients dans la base de données.
 */
public class RechercheAdminController {

    @FXML
    private Parent fxml;
    @FXML
    private AnchorPane root;
    @FXML
    private TextField nomUtilisateurField;
    @FXML
    private TextField telephoneField;
    @FXML
    private TextField adresseMailField;
    @FXML
    private TableView<ClientModel> tableView;
    @FXML
    private TableColumn<ClientModel, Integer> idCol;
    @FXML
    private TableColumn<ClientModel, String> nomUtilisateurCol;
    @FXML
    private TableColumn<ClientModel, String> telCol;
    @FXML
    private TableColumn<ClientModel, String> emailCol;
    @FXML
    private TableColumn<ClientModel, String> mdpCol;
    @FXML
    private Button rechercherButton;
    @FXML
    private Button retourButton;
    @FXML
    private ImageView retourImageView;

    private Connection connection;

    /**
     * Méthode d'initialisation appelée automatiquement lors du chargement de la vue.
     */
    @FXML
    void initialize() {
        initializeDatabase();

        // Initialisez vos colonnes de TableView ici
        // Assurez-vous d'ajouter des cell factories pour mapper les données à afficher dans la colonne

        // Exemple:
        idCol.setCellValueFactory(cellData -> cellData.getValue().getIdProperty().asObject());
        nomUtilisateurCol.setCellValueFactory(cellData -> cellData.getValue().getNomUtilisateurProperty());
        telCol.setCellValueFactory(cellData -> cellData.getValue().getTelProperty());
        emailCol.setCellValueFactory(cellData -> cellData.getValue().getEmailProperty());
        mdpCol.setCellValueFactory(cellData -> cellData.getValue().getMdpProperty());
    }

    /**
     * Initialise la connexion à la base de données.
     */
    private void initializeDatabase() {
        // Initialisez votre connexion à la base de données ici.
        // Remplacez les informations de connexion avec les vôtres.
        String url = "jdbc:mysql://localhost/luxedrive";
        String user = "root";
        String password = "";

        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'erreur de connexion à la base de données
        }
    }

    /**
     * Méthode appelée lors du clic sur le bouton de recherche.
     *
     * @param event L'événement de clic sur le bouton de recherche.
     */
    @FXML
    private void rechercher(ActionEvent event) {
        try {
            String nomUtilisateurValue = nomUtilisateurField.getText();
            String telephoneValue = telephoneField.getText();
            String adresseMailValue = adresseMailField.getText();

            // Requête SQL pour rechercher des clients
            String sql = "SELECT * FROM client WHERE nomClient LIKE ? AND tel LIKE ? AND email LIKE ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // Remplacer les paramètres dans la requête par les valeurs réelles
                preparedStatement.setString(1, "%" + nomUtilisateurValue + "%");
                preparedStatement.setString(2, "%" + telephoneValue + "%");
                preparedStatement.setString(3, "%" + adresseMailValue + "%");

                // Exécuter la requête de recherche
                ResultSet resultSet = preparedStatement.executeQuery();

                // Effacer les anciennes données de la table
                tableView.getItems().clear();

                // Ajouter les résultats de la recherche à la table
                while (resultSet.next()) {
                    ClientModel client = new ClientModel(
                            resultSet.getInt("idClient"),
                            resultSet.getString("nomClient"),
                            resultSet.getString("tel"),
                            resultSet.getString("email"),
                            resultSet.getString("motDePasse")
                    );
                    tableView.getItems().add(client);
                }

            } catch (SQLException e) {
                // Gérer les erreurs liées à la base de données
                e.printStackTrace();
                afficherErreur("Une erreur s'est produite lors de la recherche dans la base de données.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            afficherErreur("Une erreur s'est produite lors de la recherche des clients.");
        }
    }

    /**
     * Méthode appelée lors du clic sur le bouton de retour.
     *
     * @param event L'événement de clic sur le bouton de retour.
     */
    @FXML
    private void retourner(ActionEvent event) {
        loadFXML("/interfaces/Locataire.fxml");
    }

    /**
     * Charge le fichier FXML spécifié dans la vue principale.
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

    /**
     * Affiche une fenêtre d'erreur avec le message spécifié.
     *
     * @param message Le message d'erreur à afficher.
     */
    private void afficherErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
