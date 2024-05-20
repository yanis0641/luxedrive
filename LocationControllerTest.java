package test;

import static org.junit.jupiter.api.Assertions.*;
import javafx.scene.control.DateCell;
import controller.LocationController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ClasseBase.DatePickerCell;
import controller.LocationController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import controller.LocationController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

class LocationControllerTest {

    private LocationController locationController;
    @BeforeEach
    public void setUp() {
        
        locationController = new LocationController();
    }
  

    @Test
    public void testVerifierDisponibiliteChauffeur() {
        // Étant donné un état initial avec des données de chauffeur, date de début et date de fin
        String chauffeur = "Dupont Jean";
        String dateDebut = "2027-01-01";
        String dateFin = "2027-01-05";

        // Appel de la fonction à tester
        boolean result = locationController.verifierDisponibiliteChauffeur(chauffeur, dateDebut, dateFin);

        // Assertions pour vérifier le résultat
        Assertions.assertFalse(result);
        // Ajoutez d'autres assertions selon les conditions que vous souhaitez tester
    }

    @Test
    public void testVerifierDisponibiliteVoiture() {
        // Étant donné un état initial avec des données de voiture, date de début et date de fin
        String voiture = "BMW I8";
        String dateDebut = "2024-01-01";
        String dateFin = "2024-01-05";

        // Appel de la fonction à tester
        boolean result = locationController.verifierDisponibiliteVoiture(voiture, dateDebut, dateFin);

        // Assertions pour vérifier le résultat
        Assertions.assertFalse(result);
        // Ajoutez d'autres assertions selon les conditions que vous souhaitez tester
    }
}
