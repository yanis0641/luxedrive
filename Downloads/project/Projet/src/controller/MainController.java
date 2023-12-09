package controller;



import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.util.Duration;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class MainController implements Initializable {

    @FXML
    private Button btn_seconnecter;

    @FXML
    private Button btn_sinscrire;

    @FXML
    private VBox VBox;
    
    private Parent fxml;
    
    
    @FXML
    void openSignIn() {
        TranslateTransition t = new TranslateTransition(Duration.seconds(1), VBox);
        t.setToX(VBox.getLayoutX() * 7);
        t.play();
        t.setOnFinished(e -> {
   		 try {
				fxml = FXMLLoader.load(getClass().getResource("../interfaces/SignIn.fxml"));
				 VBox.getChildren().removeAll();
				 VBox.getChildren().setAll(fxml);
			} catch (Exception el) {
	
				el.printStackTrace();
			}
   		
   	 });
    }

    @FXML
    void openSignUp() {
        TranslateTransition t = new TranslateTransition(Duration.seconds(1), VBox);
        t.setToX(7);
        t.play();
        t.setOnFinished(e -> {
      		 try {
   				fxml = FXMLLoader.load(getClass().getResource("../interfaces/SignUp.fxml"));
   				 VBox.getChildren().removeAll();
   				 VBox.getChildren().setAll(fxml);
   			} catch (Exception el) {
   	
   				el.printStackTrace();
   			}
      		
      	 });
        
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TranslateTransition t = new TranslateTransition(Duration.seconds(1), VBox);
        t.setToX(VBox.getLayoutX() * 7);
        t.play();
        
        
    }
}

