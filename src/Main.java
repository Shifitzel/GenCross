// Loads menu 

import java.io.FileInputStream;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;


public class Main extends Application {
    public static String oS = "Linux";

    @Override
    public void start(Stage stage) throws Exception {
        

        Parent root = FXMLLoader.load(getClass().getResource("FXMLs/Menu.fxml")); 
        stage.setTitle("GenCross");
        stage.getIcons().add(new Image(new FileInputStream("lib/Images/logo.png")));
        stage.setScene(new Scene(root));
        stage.show();    
    }


    public static void main(String[] args) {
                launch(args);  
    }
}


