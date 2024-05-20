package ClasseBase;



public class Voiture {
private int idVoiture;
   private String couleur;
   private String immatriculation;
   private String marque;
   private float prixLocation;
   private String lienImg;
   private int id;

   // Constructeur
   public Voiture(int idVoiture, String couleur, String immatriculation, String marque, float prixLocation, String lienImg) {
       this.idVoiture = idVoiture;
       this.couleur = couleur;
       this.immatriculation = immatriculation;
       this.marque = marque;
       this.prixLocation = prixLocation;
       this.lienImg = lienImg;
   }

   // Ajoutez les getters et les setters appropriés pour chaque propriété

   public int getIdVoiture() {
       return idVoiture;
   }

   public void setIdVoiture(int idVoiture) {
       this.idVoiture = idVoiture;
   }

   public String getCouleur() {
       return couleur;
   }

   public void setCouleur(String couleur) {
       this.couleur = couleur;
   }

   public String getImmatriculation() {
       return immatriculation;
   }

   public void setImmatriculation(String immatriculation) {
       this.immatriculation = immatriculation;
   }

   public String getMarque() {
       return marque;
   }

   public void setMarque(String marque) {
       this.marque = marque;
   }

   public float getPrixLocation() {
       return prixLocation;
   }

   public void setPrixLocation(float prixLocation) {
       this.prixLocation = prixLocation;
   }

   public String getLienImg() {
       return lienImg;
   }

   public void setLienImg(String lienImg) {
       this.lienImg = lienImg;
   }

public int getId() {
   return id;
}
   
   // Vous pouvez ajouter d'autres méthodes ou fonctionnalités selon vos besoins

}
