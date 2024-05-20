package controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class RechercheClientController {
	  @FXML
	    private Parent fxml;

	    @FXML
	    private AnchorPane root;

    @FXML
    private TextField rechercheParPrixField;

    @FXML
    private ComboBox<String> marqueComboBox;

    @FXML
    private ComboBox<String> couleurComboBox;

    @FXML
    private Button rechercherButton;

    @FXML
    private TableView<ResultatVoiture> resultatTableView;

    @FXML
    private TableColumn<ResultatVoiture, String> marqueColumn;

    @FXML
    private TableColumn<ResultatVoiture, String> couleurColumn;

    @FXML
    private TableColumn<ResultatVoiture, Double> prixColumn;


    @FXML
    private void initialize() {
        chargerMarques();
        chargerCouleurs();

        // Configurer les colonnes du TableView
        marqueColumn.setCellValueFactory(new PropertyValueFactory<>("marque"));
        couleurColumn.setCellValueFactory(new PropertyValueFactory<>("couleur"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));

        // Ajouter une nouvelle colonne pour les images des voitures
        TableColumn<ResultatVoiture, ImageView> imageColumn = new TableColumn<>("Image");
        imageColumn.setCellValueFactory(cellData -> {
            Image image = new Image(new ByteArrayInputStream(cellData.getValue().getImageBytes()));
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(50); // ajustez la hauteur selon vos besoins
            imageView.setFitWidth(50); // ajustez la largeur selon vos besoins
            return new SimpleObjectProperty<>(imageView);
        });

        resultatTableView.getColumns().add(imageColumn);
    }
@FXML
    private void rechercherVoiture(ActionEvent event)  {
        String prixRecherche = rechercheParPrixField.getText();
        String marqueRecherche = marqueComboBox.getValue();
        String couleurRecherche = couleurComboBox.getValue();

        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM voituredeluxe WHERE 1=1");

        if (!prixRecherche.isEmpty()) {
            try {
                double prixValue = Double.parseDouble(prixRecherche);
                sqlBuilder.append(" AND prixLocation <= ").append(prixValue);
            } catch (NumberFormatException e) {
                afficherErreur("Le champ Prix doit être un nombre valide.");
                return;
            }
        }

        if (marqueRecherche != null && !marqueRecherche.isEmpty()) {
            sqlBuilder.append(" AND marque = '").append(marqueRecherche).append("'");
        }

        if (couleurRecherche != null && !couleurRecherche.isEmpty()) {
            sqlBuilder.append(" AND couleur = '").append(couleurRecherche).append("'");
        }

        String url = "jdbc:mysql://localhost/luxedrive";
        String user = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sqlBuilder.toString());
             ResultSet resultSet = preparedStatement.executeQuery()) {

            // Effacer les résultats précédents
            resultatTableView.getItems().clear();

            // Afficher les résultats avec les images
            while (resultSet.next()) {
                afficherResultat(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            afficherErreur("Erreur lors de la recherche des voitures. Vérifiez la console pour plus d'informations.");
        }}
   

    private void afficherErreur(String message) {
        // Afficher une boîte de dialogue d'alerte
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void afficherResultat(ResultSet resultSet) throws SQLException {
        String marque = resultSet.getString("marque");
        String couleur = resultSet.getString("couleur");
        double prix = resultSet.getDouble("prixLocation");
        byte[] imageBytes = resultSet.getBytes("lien_img"); // Assurez-vous d'avoir une colonne lien_img de type longblob dans votre base de données

        // Ajoutez le résultat à la liste des résultats du TableView
        resultatTableView.getItems().add(new ResultatVoiture(marque, couleur, prix, imageBytes));
    }

    private void chargerMarques() {
        // Chargez les marques de la base de données et ajoutez-les à la ComboBox
        String url = "jdbc:mysql://localhost/luxedrive";
        String user = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT DISTINCT marque FROM voituredeluxe";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                Set<String> marques = new HashSet<>();
                while (resultSet.next()) {
                    marques.add(resultSet.getString("marque"));
                }

                // Ajoutez les marques à la ComboBox
                ObservableList<String> options = FXCollections.observableArrayList(marques);
                marqueComboBox.setItems(options);

            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de connexion à la base de données
        }
    }

    private void chargerCouleurs() {
        // Chargez les couleurs de la base de données et ajoutez-les à la ComboBox
        String url = "jdbc:mysql://localhost/luxedrive";
        String user = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT DISTINCT couleur FROM voituredeluxe";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                Set<String> couleurs = new HashSet<>();
                while (resultSet.next()) {
                    couleurs.add(resultSet.getString("couleur"));
                }

                // Ajoutez les couleurs à la ComboBox
                ObservableList<String> options = FXCollections.observableArrayList(couleurs);
                couleurComboBox.setItems(options);

            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de connexion à la base de données
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
    @FXML
    private void retournerC(ActionEvent event) {
    	   try {
               fxml = FXMLLoader.load(getClass().getResource("/interfaces/optionVoitureClient.fxml"));
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
       }

    }


