package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Contrôleur de l'interface graphique d'accueil.
 */
public class AccueilController {

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox rootPane;

    /**
     * Initialise le contrôleur. Charge les images depuis la base de données et les affiche dans l'interface.
     */
    public void initialize() {
        scrollPane.setContent(rootPane);
        scrollPane.setPannable(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS); // Afficher toujours la barre de défilement verticale

        // Charger les images depuis la base de données
        loadImages();
    }

    /**
     * Charge les images depuis la base de données et les affiche dans l'interface.
     */
    private void loadImages() {
        // Les détails de connexion à la base de données
        String jdbcUrl = "jdbc:mysql://localhost/luxedrive";
        String user = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, user, password)) {
            String query = "SELECT lien_img, marque, prixLocation FROM voituredeluxe";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                int carsPerRow = 3;
                int carCount = 0;

                HBox currentHBox = new HBox();
                currentHBox.setAlignment(Pos.CENTER);
                currentHBox.setSpacing(40);
                rootPane.getChildren().add(currentHBox);

                while (resultSet.next()) {
                    byte[] imageData = resultSet.getBytes("lien_img");

                    if (imageData != null && imageData.length > 0) {
                        Image image = new Image(new ByteArrayInputStream(imageData));

                        // Crée une VBox pour chaque voiture avec marque et prix
                        VBox carInfo = new VBox();
                        carInfo.setAlignment(Pos.CENTER);
                        carInfo.setSpacing(10); // Espace supplémentaire

                        ImageView imageView = new ImageView(image);
                        imageView.setFitWidth(100);  // Ajuster la largeur de l'image
                        imageView.setFitHeight(100); // Ajuster la hauteur de l'image

                        String marque = resultSet.getString("marque");
                        double prix = resultSet.getDouble("prixLocation");

                        // Ajoute un événement de clic à l'image
                        imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> redirectToLocationPage(marque, prix));

                        carInfo.getChildren().addAll(imageView, new Text(marque), new Text("$" + prix));

                        // Ajoute la VBox de la voiture à la HBox actuelle
                        currentHBox.getChildren().add(carInfo);

                        carCount++;

                        // Si le nombre actuel de voitures est divisible par carsPerRow,
                        // crée une nouvelle HBox et l'ajoute à VBox
                        if (carCount % carsPerRow == 0) {
                            currentHBox = new HBox();
                            currentHBox.setAlignment(Pos.CENTER);
                            currentHBox.setSpacing(20);
                            rootPane.getChildren().add(currentHBox);
                        }
                    } else {
                        System.out.println("Données d'image vides.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Redirige vers la page de location avec les détails de la voiture sélectionnée.
     *
     * @param marque La marque de la voiture.
     * @param prix   Le prix de location de la voiture.
     */
    private void redirectToLocationPage(String marque, double prix) {
        try {
            // Charge la page location.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaces/Location.fxml"));
            Parent locationPage = loader.load();
            LocationController locationController = loader.getController();

            // Passe les données nécessaires au contrôleur de la page location
            locationController.initData(marque, prix);

            // Remplace la scène actuelle par la nouvelle scène
            Stage primaryStage = (Stage) rootPane.getScene().getWindow();
            primaryStage.setScene(new Scene(locationPage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
