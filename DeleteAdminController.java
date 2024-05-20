package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Contrôleur pour la suppression des clients.
 */
public class DeleteAdminController {

    @FXML
    private Parent fxml;

    @FXML
    private AnchorPane root;

    @FXML
    private TextField immatriculationField;
    @FXML
    private TextField idField;
    @FXML
    private Button deleteButton;
    @FXML
    private Button retourButton;
    @FXML
    private ImageView retourImageView;

    private Connection connection;

    /**
     * Méthode appelée lors de l'initialisation du contrôleur.
     * Initialise la connexion à la base de données.
     */
    @FXML
    void initialize() {
        initializeDatabase();
    }

    /**
     * Initialise la connexion à la base de données.
     * Remplacez les informations de connexion avec les vôtres.
     */
    private void initializeDatabase() {
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
     * Méthode appelée lorsqu'on clique sur le bouton de suppression.
     * Supprime un client de la base de données en utilisant l'email ou l'ID du client.
     *
     * @param event L'événement de clic sur le bouton.
     */
    @FXML
    private void supprimerClient(ActionEvent event) {
        try {
            String immatriculationValue = immatriculationField.getText();
            String idValue = idField.getText();

            // Vérifier si au moins un des champs est rempli
            if (immatriculationValue.isEmpty() && idValue.isEmpty()) {
                afficherErreur("Veuillez saisir au moins l'email ou l'ID du client.");
                return;
            }

            // Requête SQL pour supprimer un client
            String sql = "DELETE FROM client WHERE email = ? OR idClient = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // Remplacer les paramètres dans la requête par les valeurs réelles
                preparedStatement.setString(1, immatriculationValue);
                preparedStatement.setString(2, idValue);

                // Exécuter la requête de suppression
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    afficherInformation("Client(s) supprimé(s) avec succès de la base de données.");
                } else {
                    afficherErreur("Aucun client trouvé avec les informations fournies.");
                }

            } catch (SQLException e) {
                // Gérer les erreurs liées à la base de données
                e.printStackTrace();
                afficherErreur("Une erreur s'est produite lors de la suppression du client de la base de données.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            afficherErreur("Une erreur s'est produite lors de la suppression du client.");
        }
    }

    /**
     * Méthode appelée lorsqu'on clique sur le bouton de retour.
     * Redirige l'utilisateur vers l'interface Locataire.
     *
     * @param event L'événement de clic sur le bouton de retour.
     */
    @FXML
    private void retourner(ActionEvent event) {
        try {
            fxml = FXMLLoader.load(getClass().getResource("/interfaces/Locataire.fxml"));
            root.getChildren().removeAll();
            root.getChildren().setAll(fxml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Affiche une boîte de dialogue d'erreur.
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

    /**
     * Affiche une boîte de dialogue d'information.
     *
     * @param message Le message d'information à afficher.
     */
    private void afficherInformation(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
