package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * La classe Main est le point d'entrée de l'application JavaFX.
 */
public class Main extends Application {

    /**
     * La méthode start est appelée lorsque l'application JavaFX est lancée.
     *
     * @param primaryStage Le stage principal de l'application.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            // Charger le fichier FXML
            Parent root = FXMLLoader.load(getClass().getResource("../interfaces/Main.fxml"));

            // Créer une scène transparente
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);

            // Définir la scène sur le stage principal
            primaryStage.setScene(scene);

            // Définir le style du stage comme transparent
            primaryStage.initStyle(StageStyle.TRANSPARENT);

            // Afficher le stage principal
            primaryStage.show(); 
        } catch (Exception e) {
            // Imprimer la trace de la pile en cas d'exception
            e.printStackTrace();
        }
    }

    /**
     * La méthode main est le point d'entrée de l'application.
     *
     * @param args Les arguments de ligne de commande passés à l'application.
     */
    public static void main(String[] args) {
        // Lancer l'application JavaFX
        launch(args);
    }
}
