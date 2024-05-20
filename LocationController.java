/*package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

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
import java.util.HashSet;
import java.util.Set;

public class LocationController {

    private static final String JDBC_URL = "jdbc:mysql://localhost/luxedrive";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    @FXML
    private TextField nomTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private ComboBox<String> voitureComboBox;

    @FXML
    private ComboBox<String> chauffeurComboBox;

    @FXML
    private DatePicker debutDatePicker;

    @FXML
    private DatePicker finDatePicker;

    @FXML
    private CheckBox avecChauffeurCheckBox;

    @FXML
    private TextField telTextField;

    @FXML
    private Button reserverButton;


private int idClient; // Ajout de la variable pour stocker l'ID du client
private int idLocation;
long prixLocation;
int idVoiture;
private double montantTotal;

public void initIdLocation(int idLocation) {
    this.idLocation = idLocation;
    System.out.println("idLocation (initIdLocation): " + idLocation);
}

@FXML
private void initialize() {
    chargerMarques();
    chargerChauffeurs();
    // Ajout d'un ChangeListener pour la case à cocher
    avecChauffeurCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
        // Mettre à jour la visibilité du ComboBox en fonction de l'état de la case à cocher
        chauffeurComboBox.setVisible(newValue);
        chauffeurComboBox.setManaged(newValue);
        voitureComboBox.setOnAction(event -> {
            // Mettre à jour l'ID de la voiture lorsque la voiture est sélectionnée
            String selectedCar = voitureComboBox.getValue();
            idVoiture = obtenirIdVoiture(selectedCar);
        });

    });

}

@FXML
private void handleReserverButton() throws SQLException {
    if (ajouterLocation()) {
        showAlert("Location réservée avec succès !\nVotre numéro de réservation : " + idLocation);
        ajusterMontantSelonChauffeur();
        ouvrirPagePaiement();
    } else {
        showAlert("Erreur lors de la réservation de la location.");
    }
}


private void ouvrirPagePaiement() {
	 try {
	        // Dans le contrôleur de la page `Location` ou là où vous chargez la page `Paiement`
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaces/paiement.fxml"));

	        // Obtenez l'ID de la voiture à partir du nom de la voiture
	        int idVoiture = obtenirIdVoiture(voitureComboBox.getValue());
	        if (idVoiture == -1) {
	            showAlert("Erreur lors de la récupération de l'ID de la voiture.");
	            return;
	        }
	        
	        // Obtenez le prix de location à partir de l'ID de la voiture
	        double prixLocationVoiture = obtenirPrixLocation(idVoiture);
	        if (prixLocationVoiture == 0.0) {
	            showAlert("Erreur lors de la récupération du prix de location de la voiture.");
	            return;
	        }

	        paiementController paiementController = new paiementController(idLocation, montantTotal, idVoiture);
            loader.setController(paiementController);
            
	        Parent root = loader.load();

	        // Charger la page Paiement
	        Stage stage = new Stage();
	        stage.setScene(new Scene(root));
	        stage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	} 
private double calculerMontantTotal(double prixLocationVoiture, long nbJours, boolean avecChauffeur) {
 

	double montantTotal = prixLocation* nbJours;
    if (avecChauffeur) {
        montantTotal += 150.0;
    }

    return montantTotal;
}
private void ajusterMontantSelonChauffeur() {
    boolean avecChauffeur = avecChauffeurCheckBox.isSelected();
    double prixLocationVoiture = obtenirPrixLocationVoiture();
    long nbJours = obtenirNombreJours();
    montantTotal = calculerMontantTotal(prixLocationVoiture, nbJours, avecChauffeur);
}
private double obtenirPrixLocationVoiture() {
    try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
        String sql = "SELECT prixLocation FROM voituredeluxe WHERE idVoiture = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idVoiture);

            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("prixLocation");
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    // Si aucun prix n'est trouvé, retournez une valeur par défaut
    return 0.0;
}

private long obtenirNombreJours() {
    // Vous devez implémenter la logique pour obtenir le nombre de jours entre la date de début et la date de fin
    LocalDate dateDebut = obtenirDateDebutLocation();
    LocalDate dateFin = obtenirDateFinLocation();

    return ChronoUnit.DAYS.between(dateDebut, dateFin) + 1; // Ajoutez 1 pour inclure le jour de début
}
private LocalDate obtenirDateDebutLocation() {
    try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
        String sql = "SELECT dateDebut FROM location WHERE idLocation = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idLocation);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDate("dateDebut").toLocalDate();
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    // Si la récupération échoue, retournez une valeur par défaut
    return LocalDate.now(); // Vous pouvez ajuster cela en fonction de votre logique
}

private LocalDate obtenirDateFinLocation() {
    try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
        String sql = "SELECT dateFin FROM location WHERE idLocation = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idLocation);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDate("dateFin").toLocalDate();
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    

    // Si la récupération échoue, retournez une valeur par défaut
    return LocalDate.now(); // Vous pouvez ajuster cela en fonction de votre logique
}



private boolean ajouterLocation() throws SQLException {
	
    String nom = nomTextField.getText();
    String email = emailTextField.getText();
    String voiture = voitureComboBox.getValue();
    String chauffeur = chauffeurComboBox.getValue();
    String tel = telTextField.getText();


    int idVoiture = obtenirIdVoiture(voiture);
    if (idVoiture == -1) {
        showAlert("La voiture n'est pas trouvée. Veuillez sélectionner une voiture valide.");
        return false;
    }

    // Vérification du format du numéro de téléphone
    if (!tel.matches("\\d{10}")) {
        showAlert("Format de numéro de téléphone incorrect. Veuillez utiliser un format à 10 chiffres.");
        return false;
    }

    // Vérification du format de l'adresse e-mail
    if (!email.matches("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b")) {
        showAlert("Format d'adresse e-mail incorrect.");
        return false;
    }

    if (nom.isEmpty() || email.isEmpty() || voiture == null || tel.isEmpty() || debutDatePicker.getValue() == null || finDatePicker.getValue() == null) {
        showAlert("Veuillez remplir tous les champs obligatoires.");
        return false;
    }

    // Connexion du client pour obtenir son ID
    idClient = obtenirIdClient(email);
    if (idClient == -1) {
        showAlert("Le client n'est pas trouvé. Veuillez vous connecter.");
        return false;
    }

    String dateDebut = debutDatePicker.getValue().toString();
    String dateFin = finDatePicker.getValue().toString();
    boolean avecChauffeur = avecChauffeurCheckBox.isSelected();

    // Vérifier la disponibilité du chauffeur
    if (avecChauffeur && !verifierDisponibiliteChauffeur(chauffeur, dateDebut, dateFin)) {
        showAlert("Le chauffeur n'est pas disponible aux dates spécifiées.");
        return false;
    }

    // Vérifier la disponibilité de la voiture
    if (!verifierDisponibiliteVoiture(voiture, dateDebut, dateFin)) {
        showAlert("La voiture n'est pas disponible aux dates spécifiées.");
        return false;
    }

    try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
        // Ajouter la location dans la table location
        String sqlLocation = "INSERT INTO location (dateDebut, dateFin, avecChauffeur, numChauffeur, idClient, idVoiture, StatutLocation) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatementLocation = connection.prepareStatement(sqlLocation, Statement.RETURN_GENERATED_KEYS)) {
 {
            preparedStatementLocation.setString(1, dateDebut);
            preparedStatementLocation.setString(2, dateFin);
            preparedStatementLocation.setBoolean(3, avecChauffeur);

            int numChauffeur = obtenirNumChauffeur(chauffeur);
        

            preparedStatementLocation.setInt(4, numChauffeur);
            preparedStatementLocation.setInt(5, idClient); // Utilisation de l'ID du client récupéré
            preparedStatementLocation.setInt(6, idVoiture);
            preparedStatementLocation.setString(7, "En attente");

            int rowsAffectedLocation = preparedStatementLocation.executeUpdate();
            try (ResultSet generatedKeys = preparedStatementLocation.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    idLocation = generatedKeys.getInt(1);
                }
            }


            // Mise à jour de la table occupevoiture avec les nouvelles dates
            String sqlUpdateVoiture = "INSERT INTO occupevoiture (idVoiture, dateDebut, dateFin) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatementUpdateVoiture = connection.prepareStatement(sqlUpdateVoiture)) {
                preparedStatementUpdateVoiture.setInt(1, idVoiture);
                preparedStatementUpdateVoiture.setString(2, dateDebut);
                preparedStatementUpdateVoiture.setString(3, dateFin);
                preparedStatementUpdateVoiture.executeUpdate();
            }

            // Mise à jour de la table occupechauffeur avec les nouvelles dates si avecChauffeur est vrai
            if (avecChauffeur) {
                String sqlUpdateChauffeur = "INSERT INTO occupechauffeur (numChauffeur, dateDebut, dateFin) VALUES (?, ?, ?)";
                try (PreparedStatement preparedStatementUpdateChauffeur = connection.prepareStatement(sqlUpdateChauffeur)) {
                    preparedStatementUpdateChauffeur.setInt(1, numChauffeur);
                    preparedStatementUpdateChauffeur.setString(2, dateDebut);
                    preparedStatementUpdateChauffeur.setString(3, dateFin);
                    preparedStatementUpdateChauffeur.executeUpdate();
                }
            }

            return rowsAffectedLocation > 0;
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }}
}
    private void chargerMarques() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT DISTINCT marque FROM voituredeluxe";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                Set<String> marques = new HashSet<>();
                while (resultSet.next()) {
                    marques.add(resultSet.getString("marque"));
                }

                ObservableList<String> options = FXCollections.observableArrayList(marques);
                voitureComboBox.setItems(options);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void chargerChauffeurs() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT nomChauffeur FROM chauffeurs";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                Set<String> chauffeurs = new HashSet<>();
                while (resultSet.next()) {
                    chauffeurs.add(resultSet.getString("nomChauffeur"));
                }

                ObservableList<String> options = FXCollections.observableArrayList(chauffeurs);
                chauffeurComboBox.setItems(options);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   
    private void mettreAJourOccupeChauffeur(String chauffeur, String dateDebut, String dateFin) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO occupechauffeur (numChauffeur, dateDebut, dateFin) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                int numChauffeur = obtenirNumChauffeur(chauffeur);
                preparedStatement.setInt(1, numChauffeur);
                preparedStatement.setString(2, dateDebut);
                preparedStatement.setString(3, dateFin);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void mettreAJourOccupeVoiture(String voiture, String dateDebut, String dateFin) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO occupevoiture (idVoiture, dateDebut, dateFin) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                int idVoiture = obtenirIdVoiture(voiture);
                preparedStatement.setInt(1, idVoiture);
                preparedStatement.setString(2, dateDebut);
                preparedStatement.setString(3, dateFin);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean verifierDisponibiliteChauffeur(String chauffeur, String dateDebut, String dateFin) {
    
    	  try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
    	        LocalDate dateDebutFormatted = LocalDate.parse(dateDebut, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    	        LocalDate dateFinFormatted = LocalDate.parse(dateFin, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    	        String sql = "SELECT * FROM occupechauffeur WHERE numChauffeur = ? AND dateDebut <= ? AND dateFin >= ?";
    	        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
    	            int numChauffeur = obtenirNumChauffeur(chauffeur);
    	            preparedStatement.setInt(1, numChauffeur);
    	            preparedStatement.setDate(2, java.sql.Date.valueOf(dateFinFormatted));
    	            preparedStatement.setDate(3, java.sql.Date.valueOf(dateDebutFormatted));

    	            try (ResultSet resultSet = preparedStatement.executeQuery()) {
    	                if (resultSet.next()) {
    	                    showAlert("Le chauffeur n'est pas disponible aux dates spécifiées.");
    	                    return false;
    	                }
    	            }
    	        }
    	    } catch (SQLException | DateTimeParseException e) {
    	        e.printStackTrace();
    	        return false;
    	    }
    	    return true;
    	}
    private boolean verifierDisponibiliteVoiture(String voiture, String dateDebut, String dateFin) {
    	try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            LocalDate dateDebutFormatted = LocalDate.parse(dateDebut, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate dateFinFormatted = LocalDate.parse(dateFin, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            String sql = "SELECT * FROM occupevoiture WHERE idVoiture = ? AND dateDebut <= ? AND dateFin >= ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, obtenirIdVoiture(voiture));
                preparedStatement.setDate(2, java.sql.Date.valueOf(dateFinFormatted));
                preparedStatement.setDate(3, java.sql.Date.valueOf(dateDebutFormatted));

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        showAlert("La voiture n'est pas disponible aux dates spécifiées.");
                        return false;
                    }
                }
            }
        } catch (SQLException | DateTimeParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
	   
    private int obtenirNumChauffeur(String nomChauffeur) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT numChauffeur FROM chauffeurs WHERE nomChauffeur = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, nomChauffeur);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("numChauffeur");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Si aucun chauffeur n'est trouvé, retournez une valeur par défaut
        return -1;
    }

    private int obtenirIdClient(String emailClient) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT idClient FROM client WHERE email = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, emailClient);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("idClient");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Si aucun client n'est trouvé, retournez une valeur par défaut
        return -1;
    }

    private int obtenirIdVoiture(String nomVoiture) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT idVoiture FROM voituredeluxe WHERE marque = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, nomVoiture);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("idVoiture");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Si aucune voiture n'est trouvée, retournez une valeur par défaut
        return -1;
    }
    private double obtenirPrixLocation(int idVoiture) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT prixLocation FROM voituredeluxe WHERE idVoiture = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, idVoiture);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getDouble("prixLocation");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Si aucun prix n'est trouvé, retournez une valeur par défaut
        return 0.0;
    }

  

    public void initData(String marque, double prix) {
        // TODO Auto-generated method stub
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}*/
package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

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
import java.util.HashSet;
import java.util.Set;

import ClasseBase.DatePickerCell;
public class LocationController {

    private static final String JDBC_URL = "jdbc:mysql://localhost/luxedrive";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    @FXML
    private TextField nomTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private ComboBox<String> voitureComboBox;

    @FXML
    private ComboBox<String> chauffeurComboBox;

    @FXML
    private DatePicker debutDatePicker;

    @FXML
    private DatePicker finDatePicker;

    @FXML
    private CheckBox avecChauffeurCheckBox;

    @FXML
    private TextField telTextField;

    @FXML
    private Button reserverButton;

    private int idClient; // Ajout de la variable pour stocker l'ID du client
    private int idLocation;
    long prixLocation;
    int idVoiture;
    private double montantTotal;
    boolean avecChauffeur;
 public LocationController() {
    	
    }

    public void initIdLocation(int idLocation) {
        this.idLocation = idLocation;
        System.out.println("idLocation (initIdLocation): " + idLocation);
    }



    @FXML
    private void initialize() {
        chargerMarques();
        chargerChauffeurs();

        // Ajout d'un ChangeListener pour la case à cocher
        avecChauffeurCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            // Mettre à jour la visibilité du ComboBox en fonction de l'état de la case à cocher
            chauffeurComboBox.setVisible(newValue);
            chauffeurComboBox.setManaged(newValue);

            // Mettre à jour la variable avecChauffeur
            avecChauffeur = newValue;

            voitureComboBox.setOnAction(event -> {
                // Mettre à jour l'ID de la voiture lorsque la voiture est sélectionnée
                String selectedCar = voitureComboBox.getValue();
                idVoiture = obtenirIdVoiture(selectedCar);
            });
        });

        // Set the DayCellFactory to disable past dates in debutDatePicker
        debutDatePicker.setDayCellFactory(picker -> new DatePickerCell());

        // Set the DayCellFactory to disable dates before debutDatePicker's selected date in finDatePicker
        finDatePicker.setDayCellFactory(picker -> new DatePickerCell(debutDatePicker.getValue()));

        // Add a ChangeListener to update finDatePicker when debutDatePicker's value changes
        debutDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Update finDatePicker to ensure it is always after debutDatePicker
            finDatePicker.setDayCellFactory(picker -> new DatePickerCell(newValue));
        });
    }
    @FXML
    private void handleReserverButton() throws SQLException {
        if (ajouterLocation()) {
            showAlert("Location réservée avec succès !\nVotre numéro de réservation : " + idLocation);
            ouvrirPagePaiement();
        } else {
            showAlert("Erreur lors de la réservation de la location.");
        }
    }

    private void ouvrirPagePaiement() {
        try {
            // Dans le contrôleur de la page `Location` ou là où vous chargez la page `Paiement`
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaces/paiement.fxml"));

            // Obtenez l'ID de la voiture à partir du nom de la voiture
            int idVoiture = obtenirIdVoiture(voitureComboBox.getValue());
            if (idVoiture == -1) {
                showAlert("Erreur lors de la récupération de l'ID de la voiture.");
                return;
            }

            // Obtenez le prix de location à partir de l'ID de la voiture
            double prixLocationVoiture = obtenirPrixLocation(idVoiture);
            if (prixLocationVoiture == 0.0) {
                showAlert("Erreur lors de la récupération du prix de location de la voiture.");
                return;
            }
         

            paiementController paiementController = new paiementController(idLocation, montantTotal, idVoiture, avecChauffeur);
            loader.setController(paiementController);

            Parent root = loader.load();

            // Charger la page Paiement
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean ajouterLocation() throws SQLException {

        String nom = nomTextField.getText();
        String email = emailTextField.getText();
        String voiture = voitureComboBox.getValue();
        String chauffeur = chauffeurComboBox.getValue();
        String tel = telTextField.getText();

        int idVoiture = obtenirIdVoiture(voiture);
        if (idVoiture == -1) {
            showAlert("La voiture n'est pas trouvée. Veuillez sélectionner une voiture valide.");
            return false;
        }

        // Vérification du format du numéro de téléphone
        if (!tel.matches("\\d{10}")) {
            showAlert("Format de numéro de téléphone incorrect. Veuillez utiliser un format à 10 chiffres.");
            return false;
        }

        // Vérification du format de l'adresse e-mail
        if (!email.matches("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b")) {
            showAlert("Format d'adresse e-mail incorrect.");
            return false;
        }

        if (nom.isEmpty() || email.isEmpty() || voiture == null || tel.isEmpty() || debutDatePicker.getValue() == null || finDatePicker.getValue() == null) {
            showAlert("Veuillez remplir tous les champs obligatoires.");
            return false;
        }

        // Connexion du client pour obtenir son ID
        idClient = obtenirIdClient(email);
        if (idClient == -1) {
            showAlert("Le client n'est pas trouvé. Veuillez vous connecter.");
            return false;
        }

        String dateDebut = debutDatePicker.getValue().toString();
        String dateFin = finDatePicker.getValue().toString();
        boolean avecChauffeur = avecChauffeurCheckBox.isSelected();

        // Vérifier la disponibilité du chauffeur
        if (avecChauffeur && !verifierDisponibiliteChauffeur(chauffeur, dateDebut, dateFin)) {
            showAlert("Le chauffeur n'est pas disponible aux dates spécifiées.");
            return false;
        }

        // Vérifier la disponibilité de la voiture
        if (!verifierDisponibiliteVoiture(voiture, dateDebut, dateFin)) {
            showAlert("La voiture n'est pas disponible aux dates spécifiées.");
            return false;
        }

        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            // Ajouter la location dans la table location
            String sqlLocation = "INSERT INTO location (dateDebut, dateFin, avecChauffeur, numChauffeur, idClient, idVoiture, StatutLocation) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatementLocation = connection.prepareStatement(sqlLocation, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatementLocation.setString(1, dateDebut);
                preparedStatementLocation.setString(2, dateFin);
                preparedStatementLocation.setBoolean(3, avecChauffeur);

                int numChauffeur = avecChauffeur ? obtenirNumChauffeur(chauffeur) : 0; // Utilisation de l'ID du chauffeur récupéré

                preparedStatementLocation.setInt(4, numChauffeur);
                preparedStatementLocation.setInt(5, idClient); // Utilisation de l'ID du client récupéré
                preparedStatementLocation.setInt(6, idVoiture);
                preparedStatementLocation.setString(7, "En attente");

                int rowsAffectedLocation = preparedStatementLocation.executeUpdate();
                try (ResultSet generatedKeys = preparedStatementLocation.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        idLocation = generatedKeys.getInt(1);
                    }
                }

                // Mise à jour de la table occupevoiture avec les nouvelles dates
                String sqlUpdateVoiture = "INSERT INTO occupevoiture (idVoiture, dateDebut, dateFin) VALUES (?, ?, ?)";
                try (PreparedStatement preparedStatementUpdateVoiture = connection.prepareStatement(sqlUpdateVoiture)) {
                    preparedStatementUpdateVoiture.setInt(1, idVoiture);
                    preparedStatementUpdateVoiture.setString(2, dateDebut);
                    preparedStatementUpdateVoiture.setString(3, dateFin);
                    preparedStatementUpdateVoiture.executeUpdate();
                }

                // Mise à jour de la table occupechauffeur avec les nouvelles dates si avecChauffeur est vrai
                if (avecChauffeur) {
                    String sqlUpdateChauffeur = "INSERT INTO occupechauffeur (numChauffeur, dateDebut, dateFin) VALUES (?, ?, ?)";
                    try (PreparedStatement preparedStatementUpdateChauffeur = connection.prepareStatement(sqlUpdateChauffeur)) {
                        preparedStatementUpdateChauffeur.setInt(1, numChauffeur);
                        preparedStatementUpdateChauffeur.setString(2, dateDebut);
                        preparedStatementUpdateChauffeur.setString(3, dateFin);
                        preparedStatementUpdateChauffeur.executeUpdate();
                    }
                }

                // Calcul du montant total sans ajouter 150 pour le chauffeur
                double prixLocationVoiture = obtenirPrixLocation(idVoiture);
                long nbJours = obtenirNombreJours();
                montantTotal = calculerMontantTotal(prixLocationVoiture, nbJours);

                return rowsAffectedLocation > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private double calculerMontantTotal(double prixLocationVoiture, long nbJours) {
        double montantTotal = prixLocationVoiture * nbJours;
		return montantTotal;
       
     
    }
    private void ajusterMontantSelonChauffeur() {
        boolean avecChauffeur = avecChauffeurCheckBox.isSelected();
        double prixLocationVoiture = obtenirPrixLocationVoiture();
        long nbJours = obtenirNombreJours();
        montantTotal = calculerMontantTotal(prixLocationVoiture, nbJours);
    }
    private double obtenirPrixLocationVoiture() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT prixLocation FROM voituredeluxe WHERE idVoiture = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, idVoiture);

                try (var resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getDouble("prixLocation");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Si aucun prix n'est trouvé, retournez une valeur par défaut
        return 0.0;
    }

    private long obtenirNombreJours() {
        // Vous devez implémenter la logique pour obtenir le nombre de jours entre la date de début et la date de fin
        LocalDate dateDebut = obtenirDateDebutLocation();
        LocalDate dateFin = obtenirDateFinLocation();

        return ChronoUnit.DAYS.between(dateDebut, dateFin) + 1; // Ajoutez 1 pour inclure le jour de début
    }
    private LocalDate obtenirDateDebutLocation() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT dateDebut FROM location WHERE idLocation = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, idLocation);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getDate("dateDebut").toLocalDate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Si la récupération échoue, retournez une valeur par défaut
        return LocalDate.now(); // Vous pouvez ajuster cela en fonction de votre logique
    }

    private LocalDate obtenirDateFinLocation() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT dateFin FROM location WHERE idLocation = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, idLocation);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getDate("dateFin").toLocalDate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        

        // Si la récupération échoue, retournez une valeur par défaut
        return LocalDate.now(); // Vous pouvez ajuster cela en fonction de votre logique
    }



  
    
        private void chargerMarques() {
            try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
                String sql = "SELECT DISTINCT marque FROM voituredeluxe";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                     ResultSet resultSet = preparedStatement.executeQuery()) {

                    Set<String> marques = new HashSet<>();
                    while (resultSet.next()) {
                        marques.add(resultSet.getString("marque"));
                    }

                    ObservableList<String> options = FXCollections.observableArrayList(marques);
                    voitureComboBox.setItems(options);

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        private void chargerChauffeurs() {
            try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
                String sql = "SELECT nomChauffeur FROM chauffeurs";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                     ResultSet resultSet = preparedStatement.executeQuery()) {

                    Set<String> chauffeurs = new HashSet<>();
                    while (resultSet.next()) {
                        chauffeurs.add(resultSet.getString("nomChauffeur"));
                    }

                    ObservableList<String> options = FXCollections.observableArrayList(chauffeurs);
                    chauffeurComboBox.setItems(options);

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

       
        private void mettreAJourOccupeChauffeur(String chauffeur, String dateDebut, String dateFin) {
            try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
                String sql = "INSERT INTO occupechauffeur (numChauffeur, dateDebut, dateFin) VALUES (?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    int numChauffeur = obtenirNumChauffeur(chauffeur);
                    preparedStatement.setInt(1, numChauffeur);
                    preparedStatement.setString(2, dateDebut);
                    preparedStatement.setString(3, dateFin);
                    preparedStatement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        private void mettreAJourOccupeVoiture(String voiture, String dateDebut, String dateFin) {
            try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
                String sql = "INSERT INTO occupevoiture (idVoiture, dateDebut, dateFin) VALUES (?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    int idVoiture = obtenirIdVoiture(voiture);
                    preparedStatement.setInt(1, idVoiture);
                    preparedStatement.setString(2, dateDebut);
                    preparedStatement.setString(3, dateFin);
                    preparedStatement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public boolean verifierDisponibiliteChauffeur(String chauffeur, String dateDebut, String dateFin) {
        
        	  try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
        	        LocalDate dateDebutFormatted = LocalDate.parse(dateDebut, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        	        LocalDate dateFinFormatted = LocalDate.parse(dateFin, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        	        String sql = "SELECT * FROM occupechauffeur WHERE numChauffeur = ? AND dateDebut <= ? AND dateFin >= ?";
        	        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
        	            int numChauffeur = obtenirNumChauffeur(chauffeur);
        	            preparedStatement.setInt(1, numChauffeur);
        	            preparedStatement.setDate(2, java.sql.Date.valueOf(dateFinFormatted));
        	            preparedStatement.setDate(3, java.sql.Date.valueOf(dateDebutFormatted));

        	            try (ResultSet resultSet = preparedStatement.executeQuery()) {
        	                if (resultSet.next()) {
        	                    showAlert("Le chauffeur n'est pas disponible aux dates spécifiées.");
        	                    return false;
        	                }
        	            }
        	        }
        	    } catch (SQLException | DateTimeParseException e) {
        	        e.printStackTrace();
        	        return false;
        	    }
        	    return true;
        	}
        public boolean verifierDisponibiliteVoiture(String voiture, String dateDebut, String dateFin) {
        	try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
                LocalDate dateDebutFormatted = LocalDate.parse(dateDebut, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                LocalDate dateFinFormatted = LocalDate.parse(dateFin, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                String sql = "SELECT * FROM occupevoiture WHERE idVoiture = ? AND dateDebut <= ? AND dateFin >= ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setInt(1, obtenirIdVoiture(voiture));
                    preparedStatement.setDate(2, java.sql.Date.valueOf(dateFinFormatted));
                    preparedStatement.setDate(3, java.sql.Date.valueOf(dateDebutFormatted));

                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            showAlert("La voiture n'est pas disponible aux dates spécifiées.");
                            return false;
                        }
                    }
                }
            } catch (SQLException | DateTimeParseException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
    	   
        private int obtenirNumChauffeur(String nomChauffeur) {
            try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
                String sql = "SELECT numChauffeur FROM chauffeurs WHERE nomChauffeur = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, nomChauffeur);

                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            return resultSet.getInt("numChauffeur");
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Si aucun chauffeur n'est trouvé, retournez une valeur par défaut
            return -1;
        }

        private int obtenirIdClient(String emailClient) {
            try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
                String sql = "SELECT idClient FROM client WHERE email = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, emailClient);

                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            return resultSet.getInt("idClient");
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Si aucun client n'est trouvé, retournez une valeur par défaut
            return -1;
        }

        private int obtenirIdVoiture(String nomVoiture) {
            try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
                String sql = "SELECT idVoiture FROM voituredeluxe WHERE marque = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, nomVoiture);

                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            return resultSet.getInt("idVoiture");
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Si aucune voiture n'est trouvée, retournez une valeur par défaut
            return -1;
        }
        private double obtenirPrixLocation(int idVoiture) {
            try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
                String sql = "SELECT prixLocation FROM voituredeluxe WHERE idVoiture = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setInt(1, idVoiture);

                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            return resultSet.getDouble("prixLocation");
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Si aucun prix n'est trouvé, retournez une valeur par défaut
            return 0.0;
        }

      

        public void initData(String marque, double prix) {
            // TODO Auto-generated method stub
        }

        private void showAlert(String message) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }
}
