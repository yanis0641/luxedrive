
package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.nio.file.Files;

public class VoitureController {
	 @FXML
	    private Parent fxml;

	    @FXML
	    private AnchorPane root;

    @FXML
    private TextField couleur;

    @FXML
    private TextField immatriculation;

    @FXML
    private Button addButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button selectImageButton;

    @FXML
    private Button retourButton;

    @FXML
    private TextField marque;

  

    @FXML
    private ImageView imageView;

    @FXML
    private TextField prix;

    private Connection connection;
    private File selectedImageFile;

    @FXML
    void initialize() {
        initializeDatabase();
    }

    private void initializeDatabase() {
        // Initialisez votre connexion Ã  la base de donnÃ©es ici.
        // Remplacez les informations de connexion avec les vÃ´tres.
        String url = "jdbc:mysql://localhost/luxedrive";
        String user = "root";
        String password = "";

        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            // GÃ©rer l'erreur de connexion Ã  la base de donnÃ©es
        }
    }

    @FXML
    private void ajouterVoiture(ActionEvent event) {
        try {
            String marqueValue = marque.getText();
            String couleurValue = couleur.getText();
            String immatriculationValue = immatriculation.getText();
            String prixText = prix.getText();

            // VÃ©rifier si tous les champs sont remplis
            if (marqueValue.isEmpty() || couleurValue.isEmpty() || immatriculationValue.isEmpty() || prixText.isEmpty()) {
                afficherErreur("Veuillez remplir tous les champs.");
                return;
            }

            // VÃ©rifier si le prix est un nombre valide
            double prixValue;
            try {
                prixValue = Double.parseDouble(prixText);
            } catch (NumberFormatException e) {
                afficherErreur("Le champ Prix doit Ãªtre un nombre valide.");
                return;
            }

            // VÃ©rifier si une photo a Ã©tÃ© sÃ©lectionnÃ©e
            if (selectedImageFile == null) {
                afficherErreur("Veuillez sÃ©lectionner une photo.");
                return;
            }

            String sql = "INSERT INTO voituredeluxe (marque, couleur, immatriculation, prixLocation, lien_img) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setString(1, marqueValue);
                preparedStatement.setString(2, couleurValue);
                preparedStatement.setString(3, immatriculationValue);
                preparedStatement.setDouble(4, prixValue);

                try (FileInputStream inputStream = new FileInputStream(selectedImageFile)) {
                    byte[] imageBytes = new byte[(int) selectedImageFile.length()];
                    inputStream.read(imageBytes);

                    preparedStatement.setBytes(5, imageBytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                preparedStatement.executeUpdate();
            }

            // Afficher une confirmation aprÃ¨s l'ajout
            afficherInformation("Voiture ajoutÃ©e avec succÃ¨s.");

        } catch (Exception e) {
            e.printStackTrace();
            // GÃ©rer les erreurs d'ajout dans la base de donnÃ©es
            afficherErreur("Une erreur s'est produite lors de l'ajout de la voiture.");
        }
    }
          
   
    private void afficherErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void afficherInformation(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void selectImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.bmp"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        selectedImageFile = fileChooser.showOpenDialog(new Stage());
        if (selectedImageFile != null) {
            try {
                javafx.scene.image.Image image = new javafx.scene.image.Image(selectedImageFile.toURI().toURL().toString());
                imageView.setImage(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
 
    

  
    @FXML
    private void retourner(ActionEvent event) {
    	   try {
               fxml = FXMLLoader.load(getClass().getResource("/interfaces/optionVoiture.fxml"));
               root.getChildren().removeAll();
               root.getChildren().setAll(fxml);
           } catch (IOException e) {
               e.printStackTrace();
           }
       }

       private void loadFXML(String fxmlPath) {
           try {
               FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
               Parent loadedFXML = loader.load();
               root.getChildren().setAll(loadedFXML);
           } catch (IOException e) {
               e.printStackTrace();
           }
       }}


   
