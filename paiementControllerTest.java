package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import controller.paiementController;

class paiementControllerTest {

	 @Test
	    public void testValiderDonnees_NumeroCarteCorrect_CodeSecuriteCorrect() {
	        // Arrange
	        paiementController controller = new paiementController();
	        String numeroCarte = "1234 5678 9012 3456";
	        String codeSecurite = "123";

	        // Act
	        String resultat = controller.validerDonnees(numeroCarte, codeSecurite);

	        // Assert
	        assertEquals("", resultat, "Aucune erreur ne devrait être retournée.");
	    }

	    @Test
	    public void testValiderDonnees_NumeroCarteIncorrect_CodeSecuriteCorrect() {
	        // Arrange
	        paiementController controller = new paiementController();
	        String numeroCarte = "1234 5678 9012"; // Format incorrect
	        String codeSecurite = "123";

	        // Act
	        String resultat = controller.validerDonnees(numeroCarte, codeSecurite);

	        // Assert
	        assertTrue(resultat.contains("Numéro de carte incorrect"), "Erreur sur le numéro de carte attendue.");
	    }

	    @Test
	    public void testValiderDonnees_NumeroCarteCorrect_CodeSecuriteIncorrect() {
	        // Arrange
	        paiementController controller = new paiementController();
	        String numeroCarte = "1234 5678 9012 3456";
	        String codeSecurite = "12"; // Format incorrect

	        // Act
	        String resultat = controller.validerDonnees(numeroCarte, codeSecurite);

	        // Assert
	        assertTrue(resultat.contains("Code de sécurité incorrect"), "Erreur sur le code de sécurité attendue.");
	    }

	    @Test
	    public void testValiderDonnees_NumeroCarteIncorrect_CodeSecuriteIncorrect() {
	        // Arrange
	        paiementController controller = new paiementController();
	        String numeroCarte = "1234 5678 9012"; // Format incorrect
	        String codeSecurite = "12"; // Format incorrect

	        // Act
	        String resultat = controller.validerDonnees(numeroCarte, codeSecurite);

	        // Assert
	        assertTrue(resultat.contains("Numéro de carte incorrect"), "Erreur sur le numéro de carte attendue.");
	        assertTrue(resultat.contains("Code de sécurité incorrect"), "Erreur sur le code de sécurité attendue.");
	    }
	}

