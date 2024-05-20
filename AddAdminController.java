package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddAdminController {

    @FXML
    private Parent fxml;
    @FXML
    private TextField nomClient;
    @FXML
    private TextField tel;
    @FXML
    private TextField email;
    @FXML
    private PasswordField motDePasse;
    @FXML
    private Button ajouterButton;
    @FXML
    private Button retourButton;
    @FXML
    private ImageView retourImageView;
    @FXML
    private ImageView nouvellePageImageView;
    @FXML
    private AnchorPane root;

    private Connection connection;

    // Cette méthode est appelée automatiquement lorsque le fichier FXML est chargé
    @FXML
    void initialize() {
        initializeDatabase();
    }

    // Initialise la connexion à la base de données
    private void initializeDatabase() {
        // Remplacez les détails de connexion à la base de données par les vôtres
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

    // Gère l'action lorsque le bouton "Ajouter" est cliqué
    @FXML
    private void ajouter(ActionEvent event) {
        try {
            String nomClientValue = nomClient.getText();
            String telValue = tel.getText();
            String emailValue = email.getText();
            String motDePasseValue = motDePasse.getText();

            // Vérifie si tous les champs sont remplis
            if (nomClientValue.isEmpty() || telValue.isEmpty() || emailValue.isEmpty() || motDePasseValue.isEmpty()) {
                afficherErreur("Veuillez remplir tous les champs.");
                return;
            }

            // Valide le format du numéro de téléphone
            if (!validerFormatTelephone(telValue)) {
                afficherErreur("Format de numéro de téléphone incorrect.");
                return;
            }

            // Valide le format de l'adresse email
            if (!validerFormatEmail(emailValue)) {
                afficherErreur("Format d'adresse email incorrect.");
                return;
            }

            // Requête SQL pour insérer un client
            String sql = "INSERT INTO client (nomClient, tel, email, motDePasse) VALUES (?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // Remplace les espaces réservés dans la requête par les valeurs réelles
                preparedStatement.setString(1, nomClientValue);
                preparedStatement.setString(2, telValue);
                preparedStatement.setString(3, emailValue);
                preparedStatement.setString(4, motDePasseValue);

                // Exécute la requête d'insertion
                preparedStatement.executeUpdate();

                afficherInformation("Client ajouté avec succès à la base de données.");

            } catch (SQLException e) {
                // Gère les erreurs liées à la base de données
                e.printStackTrace();
                afficherErreur("Une erreur s'est produite lors de l'ajout du client dans la base de données.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            afficherErreur("Une erreur s'est produite lors de l'ajout du client.");
        }
    }

    // Gère l'action lorsque le bouton "Retourner" est cliqué
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

    // Charge un fichier FXML dans le panneau racine
    private void loadFXML(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent loadedFXML = loader.load();
            root.getChildren().setAll(loadedFXML);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Affiche un message d'erreur dans une boîte de dialogue
    private void afficherErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Affiche un message d'information dans une boîte de dialogue
    private void afficherInformation(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Valide le format d'un numéro de téléphone à l'aide d'une expression régulière
    private boolean validerFormatTelephone(String tel) {
        String regex = "^\\d{10}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(tel);
        return matcher.matches();
    }

    // Valide le format d'une adresse email à l'aide d'une expression régulière
    private boolean validerFormatEmail(String email) {
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
