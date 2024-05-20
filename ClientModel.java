package controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Modèle représentant un client.
 */
public class ClientModel {

    // Propriété représentant l'identifiant du client
    private final SimpleIntegerProperty idProperty;
   
    // Propriétés représentant les informations du client
    private final SimpleStringProperty nomUtilisateurProperty;
    private final SimpleStringProperty telProperty;
    private final SimpleStringProperty emailProperty;
    private final SimpleStringProperty mdpProperty;

    /**
     * Constructeur du modèle Client.
     *
     * @param id            Identifiant du client.
     * @param nomUtilisateur Nom d'utilisateur du client.
     * @param tel           Numéro de téléphone du client.
     * @param email         Adresse email du client.
     * @param mdp           Mot de passe du client.
     */
    public ClientModel(int id, String nomUtilisateur, String tel, String email, String mdp) {
        this.idProperty = new SimpleIntegerProperty(id);
     
        this.nomUtilisateurProperty = new SimpleStringProperty(nomUtilisateur);
        this.telProperty = new SimpleStringProperty(tel);
        this.emailProperty = new SimpleStringProperty(email);
        this.mdpProperty = new SimpleStringProperty(mdp);
    }

    /**
     * Obtient l'identifiant du client.
     *
     * @return L'identifiant du client.
     */
    public int getId() {
        return idProperty.get();
    }

    /**
     * Obtient la propriété de l'identifiant du client.
     *
     * @return La propriété de l'identifiant du client.
     */
    public SimpleIntegerProperty getIdProperty() {
        return idProperty;
    }

    /**
     * Obtient le nom d'utilisateur du client.
     *
     * @return Le nom d'utilisateur du client.
     */
    public String getNomUtilisateur() {
        return nomUtilisateurProperty.get();
    }

    /**
     * Obtient la propriété du nom d'utilisateur du client.
     *
     * @return La propriété du nom d'utilisateur du client.
     */
    public SimpleStringProperty getNomUtilisateurProperty() {
        return nomUtilisateurProperty;
    }

    /**
     * Obtient le numéro de téléphone du client.
     *
     * @return Le numéro de téléphone du client.
     */
    public String getTel() {
        return telProperty.get();
    }

    /**
     * Obtient la propriété du numéro de téléphone du client.
     *
     * @return La propriété du numéro de téléphone du client.
     */
    public SimpleStringProperty getTelProperty() {
        return telProperty;
    }

    /**
     * Obtient l'adresse email du client.
     *
     * @return L'adresse email du client.
     */
    public String getEmail() {
        return emailProperty.get();
    }

    /**
     * Obtient la propriété de l'adresse email du client.
     *
     * @return La propriété de l'adresse email du client.
     */
    public SimpleStringProperty getEmailProperty() {
        return emailProperty;
    }

    /**
     * Obtient le mot de passe du client.
     *
     * @return Le mot de passe du client.
     */
    public String getMdp() {
        return mdpProperty.get();
    }

    /**
     * Obtient la propriété du mot de passe du client.
     *
     * @return La propriété du mot de passe du client.
     */
    public SimpleStringProperty getMdpProperty() {
        return mdpProperty;
    }
}
