package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.sql.*;

public class HistoriqueController {

    @FXML
    private VBox postContainer;

    @FXML
    private TextArea postTextArea;

    @FXML
    private Button postButton;

    private Connection connection;
    @FXML
    private VBox responseContainer;

    public HistoriqueController() {
        try {
            String jdbcUrl = "jdbc:mysql://localhost/luxedrive";
            String user = "root";
            String password = "root";
            connection = DriverManager.getConnection(jdbcUrl, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'échec de la connexion à la base de données
        }
    }

    private void displayPosts() {
        postContainer.getChildren().clear();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM question")) {

            while (resultSet.next()) {
                int idQuestion = resultSet.getInt("idQuestion");
                String contenu = resultSet.getString("Contenu");
                Date datePublication = resultSet.getDate("DatePublication");

                String questionText = "ID Question: " + idQuestion +
                        "\nContenu: " + contenu + "\nDate de publication: " + datePublication;

                Label questionLabel = new Label(questionText);
                postContainer.getChildren().add(questionLabel);
            }
        } catch (SQLException e) {
           System.out.println("erreur SQL ");
           
        }
    }

    @FXML
    public void initialize() {
        displayPosts();
    }

 

    @FXML
 
    public void postMessage() {
        String message = postTextArea.getText();

        // Remplacer par la méthode réelle

        try {
            String insertQuery = "INSERT INTO question (Contenu, DatePublication) VALUES (?, NOW())";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, message);
                preparedStatement.executeUpdate();
            }

            displayPosts();
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs SQL lors de l'insertion
        }
    }


    @FXML
    private TextArea responseTextArea;

    @FXML
    private Button respondButton;

    //@FXML
    /*public void respondToQuestion() {
        String response = responseTextArea.getText();
        int selectedQuestionId = getSelectedQuestionId(); // Méthode pour obtenir l'ID de la question sélectionnée
       
        if (selectedQuestionId != -1 && !response.isEmpty()) {
            try {
                String insertQuery = "INSERT INTO reponse (idQuestion, contenu, date) VALUES (?, ?, ?, NOW())";
                try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                    preparedStatement.setInt(1, selectedQuestionId);
                   // Méthode pour obtenir l'ID de l'admin connecté
                    preparedStatement.setString(3, response);
                    preparedStatement.executeUpdate();
                }

                // Mettre à jour l'affichage des réponses après avoir répondu
                displayResponses(selectedQuestionId); // Méthode pour afficher les réponses à une question spécifique
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }*/

/*private int getSelectedQuestionId() {
// TODO Auto-generated method stub
return 0;
}*/

/*private void displayResponses(int questionId) {
   // Effacer les réponses existantes avant de charger les nouvelles
 
   // VBox responseContainer = ...; // Obtenez le conteneur de réponses

   responseContainer.getChildren().clear();

   // Récupérer les réponses depuis la base de données (de la table "reponse")
   try {
       String query = "SELECT * FROM reponse WHERE idQuestion = ?";
       try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
           preparedStatement.setInt(1, questionId);

           try (ResultSet resultSet = preparedStatement.executeQuery()) {
               while (resultSet.next()) {
                   int idReponse = resultSet.getInt("idReponse");
                 
                   String contenu = resultSet.getString("contenu");
                   Date datePublication = resultSet.getDate("date");

                   // Créer un label pour chaque réponse et l'ajouter au conteneur
                   String responseText = "ID Réponse: " + idReponse +
                           "\nContenu: " + contenu + "\nDate de publication: " + datePublication;

                   Label responseLabel = new Label(responseText);
                   responseContainer.getChildren().add(responseLabel);
               }
           }
       }
   } catch (SQLException e) {
       e.printStackTrace();
   }
}*/


}

