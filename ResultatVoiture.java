package controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Représente le résultat d'une recherche de voiture, avec des informations telles que la marque,
 * la couleur, le prix de location, et les données binaires de l'image de la voiture.
 */
public class ResultatVoiture {
    
    private String marque;
    private String couleur;
    private double prix;
    private byte[] imageBytes;

    /**
     * Constructeur de la classe ResultatVoiture.
     *
     * @param marque La marque de la voiture.
     * @param couleur La couleur de la voiture.
     * @param prix Le prix de location de la voiture.
     * @param imageBytes Les données binaires de l'image de la voiture.
     */
    public ResultatVoiture(String marque, String couleur, double prix, byte[] imageBytes) {
        this.marque = marque;
        this.couleur = couleur;
        this.prix = prix;
        this.imageBytes = imageBytes;
    }

    /**
     * Obtient les données binaires de l'image de la voiture.
     *
     * @return Les données binaires de l'image de la voiture.
     */
    public byte[] getImageBytes() {
        return imageBytes;
    }

    /**
     * Définit les données binaires de l'image de la voiture.
     *
     * @param imageBytes Les données binaires de l'image de la voiture.
     */
    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    /**
     * Obtient la marque de la voiture.
     *
     * @return La marque de la voiture.
     */
    public String getMarque() {
        return marque;
    }

    /**
     * Définit la marque de la voiture.
     *
     * @param marque La marque de la voiture.
     */
    public void setMarque(String marque) {
        this.marque = marque;
    }

    /**
     * Obtient la couleur de la voiture.
     *
     * @return La couleur de la voiture.
     */
    public String getCouleur() {
        return couleur;
    }

    /**
     * Définit la couleur de la voiture.
     *
     * @param couleur La couleur de la voiture.
     */
    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    /**
     * Obtient le prix de location de la voiture.
     *
     * @return Le prix de location de la voiture.
     */
    public double getPrix() {
        return prix;
    }

    /**
     * Définit le prix de location de la voiture.
     *
     * @param prix Le prix de location de la voiture.
     */
    public void setPrix(double prix) {
        this.prix = prix;
    }
}
