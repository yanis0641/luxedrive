package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * La classe Voiture représente une voiture avec ses caractéristiques telles que l'identifiant, la couleur,
 * l'immatriculation, la marque, le prix de location et le lien vers l'image.
 */
public class Voiture {
    private int idVoiture;
    private String couleur;
    private String immatriculation;
    private String marque;
    private double prixLocation;
    private String lienImg;

    /**
     * Constructeur de la classe Voiture.
     *
     * @param idVoiture      L'identifiant de la voiture.
     * @param couleur        La couleur de la voiture.
     * @param immatriculation L'immatriculation de la voiture.
     * @param marque         La marque de la voiture.
     * @param prixLocation   Le prix de location de la voiture.
     * @param lienImg        Le lien vers l'image de la voiture.
     */
    public Voiture(int idVoiture, String couleur, String immatriculation, String marque, double prixLocation, String lienImg) {
        this.idVoiture = idVoiture;
        this.couleur = couleur;
        this.immatriculation = immatriculation;
        this.marque = marque;
        this.prixLocation = prixLocation;
        this.lienImg = lienImg;
    }

    /**
     * Getter pour l'identifiant de la voiture.
     *
     * @return L'identifiant de la voiture.
     */
    public int getIdVoiture() {
        return idVoiture;
    }

    /**
     * Setter pour l'identifiant de la voiture.
     *
     * @param idVoiture Nouvel identifiant de la voiture.
     */
    public void setIdVoiture(int idVoiture) {
        this.idVoiture = idVoiture;
    }

    /**
     * Getter pour la couleur de la voiture.
     *
     * @return La couleur de la voiture.
     */
    public String getCouleur() {
        return couleur;
    }

    /**
     * Setter pour la couleur de la voiture.
     *
     * @param couleur Nouvelle couleur de la voiture.
     */
    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    /**
     * Getter pour l'immatriculation de la voiture.
     *
     * @return L'immatriculation de la voiture.
     */
    public String getImmatriculation() {
        return immatriculation;
    }

    /**
     * Setter pour l'immatriculation de la voiture.
     *
     * @param immatriculation Nouvelle immatriculation de la voiture.
     */
    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    /**
     * Getter pour la marque de la voiture.
     *
     * @return La marque de la voiture.
     */
    public String getMarque() {
        return marque;
    }

    /**
     * Setter pour la marque de la voiture.
     *
     * @param marque Nouvelle marque de la voiture.
     */
    public void setMarque(String marque) {
        this.marque = marque;
    }

    /**
     * Getter pour le prix de location de la voiture.
     *
     * @return Le prix de location de la voiture.
     */
    public double getPrixLocation() {
        return prixLocation;
    }

    /**
     * Setter pour le prix de location de la voiture.
     *
     * @param prixLocation Nouveau prix de location de la voiture.
     */
    public void setPrixLocation(double prixLocation) {
        this.prixLocation = prixLocation;
    }

    /**
     * Getter pour le lien vers l'image de la voiture.
     *
     * @return Le lien vers l'image de la voiture.
     */
    public String getLienImg() {
        return lienImg;
    }

    /**
     * Setter pour le lien vers l'image de la voiture.
     *
     * @param lienImg Nouveau lien vers l'image de la voiture.
     */
    public void setLienImg(String lienImg) {
        this.lienImg = lienImg;
    }

    /**
     * Méthode statique pour récupérer les informations sur les voitures depuis la base de données.
     *
     * @return Une liste d'objets Voiture contenant les informations récupérées.
     */
    public static List<Voiture> getInformationsVoitures() {
        List<Voiture> voitures = new ArrayList<>();

        // Utilisez les informations de connexion appropriées
        String url = "jdbc:mysql://localhost:3306/luxedrive";
        String user = "root";
        String passwd = "";

        try (Connection connection = DriverManager.getConnection(url, user, passwd)) {
            String query = "SELECT idVoiture, lien_img, marque, prixLocation FROM voituredeluxe";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    int idVoiture = resultSet.getInt("idVoiture");
                    String lienImg = resultSet.getString("lien_img");
                    String marque = resultSet.getString("marque");
                    float prixLocation = resultSet.getFloat("prixLocation");

                    Voiture voiture = new Voiture(idVoiture, "", "", marque, prixLocation, lienImg);
                    voitures.add(voiture);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return voitures;
    }

    /**
     * Méthode principale (main) pour tester la récupération des informations sur les voitures.
     *
     * @param args Arguments de la ligne de commande (non utilisés).
     */
    public static void main(String[] args) {
        List<Voiture> voitures = getInformationsVoitures();

        for (Voiture voiture : voitures) {
            System.out.println("ID: " + voiture.getIdVoiture());
            System.out.println("Marque: " + voiture.getMarque());
            System.out.println("Prix: " + voiture.getPrixLocation());
            System.out.println("Image: " + voiture.getLienImg());
            System.out.println("---------------");
        }
    }
}
