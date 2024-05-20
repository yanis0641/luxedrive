package controller;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.regex.Pattern;

/**
 * Contrôleur pour la vue de paiement.
 * Gère les opérations liées au processus de paiement.
 */
public class paiementController {
    @FXML
    private AnchorPane root;

    @FXML
    private TextField montantTextField;

    @FXML
    private TextField numeroCarteTextField;

    @FXML
    private DatePicker dateExpirationDatePicker;

    @FXML
    private TextField codeSecuriteTextField;

    @FXML
    private Button effectuerPaiementButton;

    private int idVoiture;
    private int idLocation;
    private double montantTotal;
    private boolean avecChauffeur;

    /**
     * Constructeur par défaut.
     */
    public paiementController() {
    }

    /**
     * Constructeur avec paramètres.
     *
     * @param idLocation    L'identifiant de la location.
     * @param montantTotal  Le montant total du paiement.
     * @param idVoiture     L'identifiant de la voiture liée à la location.
     * @param avecChauffeur Indique si la location inclut un chauffeur.
     */
    public paiementController(int idLocation, double montantTotal, int idVoiture, boolean avecChauffeur) {
        this.idLocation = idLocation;
        this.montantTotal = montantTotal;
        this.idVoiture = idVoiture;
        this.avecChauffeur = avecChauffeur;
    }

    String JDBC_URL = "jdbc:mysql://localhost/luxedrive"; // Mettez votre URL de base de données
    String DB_USER = "root"; // Mettez votre nom d'utilisateur
    String DB_PASSWORD = ""; // Mettez votre mot de passe

    /**
     * Méthode d'initialisation appelée automatiquement lors du chargement de la vue.
     */
    @FXML
    private void initialize() {
        numeroCarteTextField.setPromptText("Ex. 1234 5678 9012 3456");

        codeSecuriteTextField.setPromptText("VVV");
        montantTextField.setEditable(false);

        initializeMontant();
        System.out.println("Avec chauffeur : " + avecChauffeur);
    }

    /**
     * Initialise le montant total en tenant compte de la présence d'un chauffeur.
     */
    private void initializeMontant() {
        double montantAffiche1 = montantTotal + 150.0;
        double montantAffiche = avecChauffeur ? montantAffiche1 : montantTotal;
        montantTextField.setText(new DecimalFormat("0.00").format(montantAffiche));
    }

    /**
     * Méthode appelée lors de l'effet du paiement.
     */
    @FXML
    private void effectuerPaiement() {
        String montant = montantTextField.getText();
        String numeroCarte = numeroCarteTextField.getText();
        LocalDate dateExpiration = dateExpirationDatePicker.getValue();
        String codeSecurite = codeSecuriteTextField.getText();
        LocalDate dateDePaiement = LocalDate.now();

        if (enregistrerPaiement(montant, dateDePaiement, idLocation)) {
            System.out.println("Paiement effectué avec succès !");
            mettreAJourStatutLocation();
        } else {
            afficherErreur("Erreur lors de l'enregistrement du paiement.");
        }
    }

    /**
     * Valide les données du formulaire.
     *
     * @param numeroCarte   Le numéro de carte saisi par l'utilisateur.
     * @param codeSecurite  Le code de sécurité saisi par l'utilisateur.
     * @return              Un message d'erreur le cas échéant.
     */
    public String validerDonnees(String numeroCarte, String codeSecurite) {
        StringBuilder erreurMessage = new StringBuilder();

        if (!validerNumeroCarte(numeroCarte)) {
            erreurMessage.append("Numéro de carte incorrect. Utilisez un format à 16 chiffres séparés par des espaces (1234 5678 9012 3456).\n");
        }

        if (!validerCodeSecurite(codeSecurite)) {
            erreurMessage.append("Code de sécurité incorrect. Utilisez un code à 3 chiffres.\n");
        }

        return erreurMessage.toString();
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

    /**
     * Met à jour le statut de la location dans la base de données.
     */
    private void mettreAJourStatutLocation() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String sql = "UPDATE location SET statutLocation = 'validé' WHERE idLocation = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, idLocation);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Statut de la location mis à jour avec succès !");
                } else {
                    System.out.println("Erreur lors de la mise à jour du statut de la location.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Valide le montant saisi par l'utilisateur.
     *
     * @param montant Le montant à valider.
     * @return        True si le montant est valide, sinon False.
     */
    private boolean validerMontant(String montant) {
        return Pattern.matches("^\\d+(\\.\\d{1,2})?$", montant);
    }

    /**
     * Valide le format du numéro de carte saisi par l'utilisateur.
     *
     * @param numeroCarte Le numéro de carte à valider.
     * @return            True si le format est valide, sinon False.
     */
    private boolean validerNumeroCarte(String numeroCarte) {
        return Pattern.matches("^\\d{4} \\d{4} \\d{4} \\d{4}$", numeroCarte);
    }

    /**
     * Valide le format du code de sécurité saisi par l'utilisateur.
     *
     * @param codeSecurite Le code de sécurité à valider.
     * @return             True si le format est valide, sinon False.
     */
    private boolean validerCodeSecurite(String codeSecurite) {
        return Pattern.matches("^\\d{3}$", codeSecurite);
    }

    /**
     * Enregistre le paiement dans la base de données.
     *
     * @param montant       Le montant du paiement.
     * @param dateDePaiement La date du paiement.
     * @param idLocation    L'identifiant de la location associée au paiement.
     * @return              True si l'enregistrement est réussi, sinon False.
     */
    private boolean enregistrerPaiement(String montant, LocalDate dateDePaiement, int idLocation) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            montant = montant.replace(",", ".");
            String sql = "INSERT INTO paiement (Montant, dateDePaiement, idLocation) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, montant);
                preparedStatement.setDate(2, java.sql.Date.valueOf(dateDePaiement));
                preparedStatement.setInt(3, idLocation);

                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
