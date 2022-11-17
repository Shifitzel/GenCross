import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.awt.Desktop;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;




public class MenuController {
    
    public static Stage menuControllerstage = new Stage();

    @FXML
    private TextField textField;


    @FXML
    public void makeNewCrossword() { 
        TileMakerContext tiles = new TileMakerContext();
        menuControllerstage.setScene(tiles.scene);
        menuControllerstage.setTitle("Make New Crossword");
        menuControllerstage.show();
    }


    @FXML
    public void loadCrossword() {
        PlayedTiles tiles = new PlayedTiles(1,null);
        menuControllerstage.setScene(tiles.scene);
        menuControllerstage.setTitle("Load Crossword");
        menuControllerstage.show();
    }

    @FXML
    public void generateCrossword() {
        try {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLs/Info.fxml")); 
        Stage stage = new Stage();
        stage.setTitle("Info");
        stage.setScene(new Scene(root));
        stage.show();  }
        catch (IOException e) {e.printStackTrace();}

    }

    @FXML
    public void sendToForm() {
        try {
        Desktop.getDesktop().browse(new URI("https://forms.gle/GsWT9pu6ncixyqbQ8")); }
        catch (IOException e) {
            e.printStackTrace();
        }  
        catch (URISyntaxException f) {
            f.printStackTrace();
        }    
    }

    @FXML
    public void onEnterCrosswordPressed(){
        PlayedTiles tiles = new PlayedTiles(2,textField.getText());
        menuControllerstage.setScene(tiles.scene);
        menuControllerstage.setTitle("Load Crossword");
        menuControllerstage.show();
    }    

    @FXML 
    public void onInfoPressed(){
            try {
            Parent root = FXMLLoader.load(getClass().getResource("FXMLs/GenCrossInfo.fxml")); 
            Stage stage = new Stage();
            stage.setTitle("GenCrossInfo");
            stage.setScene(new Scene(root));
            stage.show();  }
            catch (IOException e) {e.printStackTrace();}
    
        }



}
