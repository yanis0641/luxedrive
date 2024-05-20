
package controller;

import java.net.URL;

import java.util.ResourceBundle;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;



public class HomeController implements Initializable {

private Parent fxml;

@FXML 
private AnchorPane root;

@FXML
   private Label labelNomUtilisateur;

   private String loggedInUserName;

   public void setLoggedInUserName(String userName) {
       this.loggedInUserName = userName;
       labelNomUtilisateur.setText("Bienvenue");

   }
   public AnchorPane getRoot() {
       return root;
   }

   @FXML
   void accueil(MouseEvent event) {
    try {
fxml = FXMLLoader.load(getClass().getResource("/interfaces/Accueil.fxml"));
root.getChildren().removeAll();
root.getChildren().setAll(fxml);
} catch (IOException e) {
// TODO Auto-generated catch block
e.printStackTrace();
}


   }

   

   

   @FXML
   void facture(MouseEvent event) {
    try {
fxml = FXMLLoader.load(getClass().getResource("/interfaces/annulation.fxml"));
root.getChildren().removeAll();
root.getChildren().setAll(fxml);
} catch (IOException e) {
// TODO Auto-generated catch block
e.printStackTrace();
}

   }

   @FXML
   void historiques(MouseEvent event) {
    try {
fxml = FXMLLoader.load(getClass().getResource("/interfaces/Historique.fxml"));
root.getChildren().removeAll();
root.getChildren().setAll(fxml);
} catch (IOException e) {
// TODO Auto-generated catch block
e.printStackTrace();
}

   }

   @FXML
   void locataire(MouseEvent event) {
    try {
fxml = FXMLLoader.load(getClass().getResource("/interfaces/Locataire.fxml"));
root.getChildren().removeAll();
root.getChildren().setAll(fxml);
} catch (IOException e) {
// TODO Auto-generated catch block
e.printStackTrace();
}

   }

   @FXML
   void location(MouseEvent event) {
    try {
fxml = FXMLLoader.load(getClass().getResource("/interfaces/Location.fxml"));
root.getChildren().removeAll();
root.getChildren().setAll(fxml);
} catch (IOException e) {
// TODO Auto-generated catch block
e.printStackTrace();
}

   }

   @FXML
   void voiture(MouseEvent event) {
    try {
fxml = FXMLLoader.load(getClass().getResource("/interfaces/optionVoiture.fxml"));
root.getChildren().removeAll();
root.getChildren().setAll(fxml);
} catch (IOException e) {
// TODO Auto-generated catch block
e.printStackTrace();
}

   }
   
   
   
   @FXML
   void accueilC(MouseEvent event) {
    try {
fxml = FXMLLoader.load(getClass().getResource("/interfaces/Accueil.fxml"));
root.getChildren().removeAll();
root.getChildren().setAll(fxml);
} catch (IOException e) {
// TODO Auto-generated catch block
e.printStackTrace();
}


   }

   

   

   @FXML
   void factureC(MouseEvent event) {
    try {
fxml = FXMLLoader.load(getClass().getResource("/interfaces/annulation.fxml"));
root.getChildren().removeAll();
root.getChildren().setAll(fxml);
} catch (IOException e) {
// TODO Auto-generated catch block
e.printStackTrace();
}

   }

   @FXML
   void historiquesC(MouseEvent event) {
    try {
fxml = FXMLLoader.load(getClass().getResource("/interfaces/Historique.fxml"));
root.getChildren().removeAll();
root.getChildren().setAll(fxml);
} catch (IOException e) {
// TODO Auto-generated catch block
e.printStackTrace();
}

   }

   @FXML
   void locataireC(MouseEvent event) {
    try {
fxml = FXMLLoader.load(getClass().getResource("/interfaces/Locataire.fxml"));
root.getChildren().removeAll();
root.getChildren().setAll(fxml);
} catch (IOException e) {
// TODO Auto-generated catch block
e.printStackTrace();
}

   }

   @FXML
   void locationC(MouseEvent event) {
    try {
fxml = FXMLLoader.load(getClass().getResource("/interfaces/Location.fxml"));
root.getChildren().removeAll();
root.getChildren().setAll(fxml);
} catch (IOException e) {
// TODO Auto-generated catch block
e.printStackTrace();
}

   }

   @FXML
   void voitureC(MouseEvent event) {
    try {
fxml = FXMLLoader.load(getClass().getResource("/interfaces/optionVoitureClient.fxml"));
root.getChildren().removeAll();
root.getChildren().setAll(fxml);
} catch (IOException e) {
// TODO Auto-generated catch block
e.printStackTrace();
}

   }
   @FXML
   void logout(MouseEvent event) {
       try {
           // Charger le fichier FXML associÃ© Ã  Main.fxml
           fxml = FXMLLoader.load(getClass().getResource("/interfaces/Main.fxml"));

           // RÃ©cupÃ©rer la scÃ¨ne Ã  partir du nÅ“ud racine (root)
           AnchorPane currentRoot = (AnchorPane) root.getScene().getRoot();

           // Remplacer le nÅ“ud racine actuel par celui du Main.fxml
           currentRoot.getChildren().setAll(fxml);
       } catch (IOException e) {
           e.printStackTrace();
       }
   }
   private void loadFXML(String fxmlPath) {
       try {
           FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
           Parent loadedFXML = loader.load();
           root.getChildren().setAll(loadedFXML);
       } catch (IOException e) {
           e.printStackTrace();
       }
   }








@Override
public void initialize(URL arg0, ResourceBundle arg1) {

}

}