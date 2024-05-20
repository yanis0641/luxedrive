package test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import controller.AnnulationReservatonController;

class AnnulationReservatonControllerTest {

	   private static final String VALID_RESERVATION_ID = "1"; // Remplacez par un ID existant dans votre base de données
	    private static final String INVALID_RESERVATION_ID = "999";
	    @Test
	    public void testAnnulerReservationReussite() {
	        AnnulationReservatonController controller = new AnnulationReservatonController();
	        boolean result = controller.annulerReservation(VALID_RESERVATION_ID);
	        assertTrue(result);
	    }

	    @Test
	    public void testAnnulerReservationEchec() {
	        AnnulationReservatonController controller = new AnnulationReservatonController();
	        boolean result = controller.annulerReservation(INVALID_RESERVATION_ID);
	        assertFalse(result);
	    }

	    @Test
	    public void testObtenirIdReservation() {
	        AnnulationReservatonController controller = new AnnulationReservatonController();
	        int result = controller.obtenirIdReservation(VALID_RESERVATION_ID);
	        assertTrue(result > 0); // Assurez-vous que l'ID obtenu est positif, indiquant qu'il a été trouvé dans la base de données
	    }

	   @Test
	    public void testObtenirNumChauffeurPourReservation() {
	        AnnulationReservatonController controller = new AnnulationReservatonController();
	        int idReservation = controller.obtenirIdReservation(VALID_RESERVATION_ID);
	        int result = controller.obtenirNumChauffeurPourReservation(idReservation);
	        assertTrue(result >= 0); // Assurez-vous que le numéro du chauffeur obtenu est non négatif
	    }
	}

