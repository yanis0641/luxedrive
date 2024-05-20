package test;
import controller.AddAdminController;
import static org.junit.jupiter.api.Assertions.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

class AddAdminControllerTest {
	 // Valide le format d'un numéro de téléphone à l'aide d'une expression régulière
    private boolean validerFormatTelephone(String tel) {
        String regex = "^\\d{10}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(tel);
        return matcher.matches();
    }

    // Valide le format d'une adresse email à l'aide d'une expression régulière
    private boolean validerFormatEmail(String email) {
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Test
    public void testValiderFormatTelephoneValide() {
        assertTrue(validerFormatTelephone("1234567890"));
        assertTrue(validerFormatTelephone("9876543210"));
    }

    @Test
    public void testValiderFormatTelephoneInvalide() {
        assertFalse(validerFormatTelephone("12345")); // moins de 10 chiffres
        assertFalse(validerFormatTelephone("abcdefghij")); // non numérique
        assertFalse(validerFormatTelephone("12a3456789")); // présence de caractères non numériques
    }

    @Test
    public void testValiderFormatEmailValide() {
        assertTrue(validerFormatEmail("john.doe@example.com"));
        assertTrue(validerFormatEmail("alice.smith123@example.co.uk"));
    }

    @Test
    public void testValiderFormatEmailInvalide() {
        assertFalse(validerFormatEmail("john.doe@.com")); // domaine manquant
        assertFalse(validerFormatEmail("invalid-email")); // format général incorrect
        assertFalse(validerFormatEmail("user@domain@.com")); // deux @ dans l'adresse email
    }
}


