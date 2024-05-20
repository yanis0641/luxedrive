package controller;

import javafx.scene.control.*;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Contrôleur associé à l'interface de connexion (SignIn.fxml).
 * Gère les actions liées à la connexion, la récupération de mot de passe, et la navigation vers la page d'accueil.
 */
public class SignInController implements Initializable {

    @FXML
	public TextField txt_userName;

    @FXML
	public PasswordField txt_password;

    @FXML
    private Button btn_passwordForgoten;

    @FXML
    private Button btn_seconnecter;

    @FXML
    private VBox VBox;
    private Parent fxml;

    private String loggedInUserName;
    public SignInController() {
    	
    }

    /**
     * Ouvre la page d'accueil en fonction des informations de connexion fournies.
     * Vérifie les informations de connexion pour les clients et les administrateurs.
     * Affiche une boîte de dialogue d'erreur en cas d'échec de l'authentification.
     */
    @FXML
    void openHome() {
        // Connexion à la base de données
        String url = "jdbc:mysql://localhost/luxedrive";
        String user = "root";
        String passwd = "";

        try (Connection connection = DriverManager.getConnection(url, user, passwd)) {
            String clientQuery = "SELECT * FROM client WHERE email = ? AND motDePasse = ?";
            String adminQuery = "SELECT * FROM admin WHERE mail = ? AND motDePasse = ?";

            // Vérifie si l'utilisateur est un client
            if (isUserInTable(connection, clientQuery)) {
                loadHomePage("/interfaces/HomeClient.fxml");
            }
            // Vérifie si l'utilisateur est un administrateur
            else if (isUserInTable(connection, adminQuery)) {
                loadHomePage("/interfaces/Home.fxml");
            } else {
                showErrorDialog("Erreur d'authentification", "Adresse e-mail ou mot de passe incorrect.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Erreur", "Une erreur s'est produite lors de l'authentification.");
        }
    }

    /**
     * Vérifie si l'utilisateur avec l'adresse e-mail et le mot de passe fournis existe dans la base de données.
     *
     * @param connection La connexion à la base de données.
     * @param query      La requête SQL pour vérifier l'utilisateur.
     * @return true si l'utilisateur existe, sinon false.
     * @throws Exception En cas d'erreur lors de l'exécution de la requête SQL.
     */
    public boolean isUserInTable(Connection connection, String query) throws Exception {
        PreparedStatement statement = connection.prepareStatement(query);
        String email = txt_userName.getText();
        String motDePasse = txt_password.getText();
        statement.setString(1, email);
        statement.setString(2, motDePasse);

        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }

    /**
     * Charge la page d'accueil correspondante en fonction du type d'utilisateur (client ou administrateur).
     *
     * @param cheminFxml Le chemin du fichier FXML de la page d'accueil.
     * @throws IOException En cas d'erreur lors du chargement du fichier FXML.
     */
    private void loadHomePage(String cheminFxml) throws IOException {
        loggedInUserName = getLoggedInUserName(); // Supposant qu'il existe une méthode pour obtenir le nom complet de l'utilisateur
        VBox.getScene().getWindow().hide();
        Stage home = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(cheminFxml));
        Parent fxml = loader.load();
        HomeController homeController = loader.getController();
        homeController.setLoggedInUserName(loggedInUserName);

        Scene scene = new Scene(fxml);
        home.setScene(scene);
        home.show();
    }

    /**
     * Lance la procédure de récupération de mot de passe.
     * Affiche une boîte de dialogue demandant à l'utilisateur d'entrer son adresse e-mail.
     */
    @FXML
    void SendPassword() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Mot de passe oublié");
        dialog.setHeaderText(null);
        dialog.setContentText("Veuillez entrer votre adresse e-mail :");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(email -> {
            System.out.println("L'utilisateur a demandé la récupération du mot de passe pour l'adresse : " + email);
        });
    }

    /**
     * Affiche une boîte de dialogue d'erreur avec le titre et le message spécifiés.
     *
     * @param title   Le titre de la boîte de dialogue d'erreur.
     * @param message Le message d'erreur à afficher.
     */
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        System.out.println("Erreur : " + message);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // Initialisation, si nécessaire
    }

    /**
     * Obtient le nom complet de l'utilisateur connecté.
     *
     * @return Le nom complet de l'utilisateur connecté.
     */
    public String getLoggedInUserName() {
        return loggedInUserName;
    }
}
