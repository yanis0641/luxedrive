package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Contrôleur pour la fenêtre d'annulation de réservation.
 */
public class AnnulationReservatonController {

    // Informations de connexion à la base de données
    private static final String JDBC_URL = "jdbc:mysql://localhost/luxedrive";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    public AnnulationReservatonController() {
    	
    }

    // ComboBox pour afficher les réservations en attente
    @FXML
    private ComboBox<String> reservationsComboBox;

    /**
     * Méthode appelée lors de l'initialisation du contrôleur.
     * Charge les réservations en attente dans la ComboBox.
     */
    @FXML
    private void initialize() {
        chargerReservations();
    }

    /**
     * Méthode appelée lorsqu'on clique sur le bouton d'annulation.
     * Annule la réservation sélectionnée et libère le chauffeur et la voiture associés.
     */
    @FXML
    private void handleAnnulationButton() {
        String selectedReservation = reservationsComboBox.getValue();
        if (selectedReservation != null && annulerReservation(selectedReservation)) {
            showAlert("Réservation annulée avec succès !");
            libererChauffeurEtVoiture(selectedReservation);
            // Vous pouvez ajouter d'autres actions ici, par exemple, fermer la fenêtre
            Stage stage = (Stage) reservationsComboBox.getScene().getWindow();
            stage.close();
        } else {
            showAlert("Erreur lors de l'annulation de la réservation.");
        }
    }

    /**
     * Charge les réservations en attente depuis la base de données et les ajoute à la ComboBox.
     */
    private void chargerReservations() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT idLocation FROM location WHERE StatutLocation = 'En attente'";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    reservationsComboBox.getItems().add(resultSet.getString("idLocation"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Annule la réservation spécifiée dans la base de données.
     *
     * @param reservation Identifiant de la réservation à annuler.
     * @return true si l'annulation a réussi, false sinon.
     */
    public boolean annulerReservation(String reservation) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String sql = "UPDATE location SET StatutLocation = 'Annulée' WHERE idLocation = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, reservation);
                int rowsAffected = preparedStatement.executeUpdate();

                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Libère le chauffeur et la voiture associés à la réservation spécifiée dans la base de données.
     *
     * @param reservation Identifiant de la réservation.
     */
    private void libererChauffeurEtVoiture(String reservation) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            int idReservation = obtenirIdReservation(reservation);

            int numChauffeur = obtenirNumChauffeurPourReservation(idReservation);
            int idVoiture = obtenirIdVoiturePourReservation(idReservation);

            String sqlChauffeur = "DELETE FROM occupechauffeur WHERE numChauffeur = ?";
            String sqlVoiture = "DELETE FROM occupevoiture WHERE idVoiture = ?";

            try (PreparedStatement preparedStatementChauffeur = connection.prepareStatement(sqlChauffeur);
                 PreparedStatement preparedStatementVoiture = connection.prepareStatement(sqlVoiture)) {
                preparedStatementChauffeur.setInt(1, numChauffeur);
                preparedStatementVoiture.setInt(1, idVoiture);

                preparedStatementChauffeur.executeUpdate();
                preparedStatementVoiture.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtient l'identifiant de la réservation à partir de son numéro.
     *
     * @param reservation Numéro de la réservation.
     * @return L'identifiant de la réservation.
     */
    public int obtenirIdReservation(String reservation) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT idLocation FROM location WHERE idLocation = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, reservation);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("idLocation");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * Obtient le numéro du chauffeur associé à la réservation.
     *
     * @param idReservation Identifiant de la réservation.
     * @return Numéro du chauffeur.
     */
    public int obtenirNumChauffeurPourReservation(int idReservation) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT numChauffeur FROM location WHERE idLocation = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, idReservation);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("numChauffeur");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * Obtient l'identifiant de la voiture associée à la réservation.
     *
     * @param idReservation Identifiant de la réservation.
     * @return Identifiant de la voiture.
     */
    private int obtenirIdVoiturePourReservation(int idReservation) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT idVoiture FROM location WHERE idLocation = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, idReservation);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("idVoiture");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * Affiche une boîte de dialogue d'information.
     *
     * @param message Le message à afficher.
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
