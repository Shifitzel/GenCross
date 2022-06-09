// Loads menu 

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;


public class Main extends Application {


    @Override
    public void start(Stage stage) throws Exception {


        Parent root = FXMLLoader.load(getClass().getResource("FXMLs/Menu.fxml")); 
        stage.setTitle("GenCross");
        stage.setScene(new Scene(root));
        stage.show();    
    }

    public static void main(String[] args) {
                launch(args);   
    }

}