import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.awt.Desktop;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MenuController {
    
    public static Stage menuControllerstage = new Stage();


    @FXML
    public void makeNewCrossword() { 
        TileMaker tiles = new TileMaker();
        menuControllerstage.setScene(tiles.scene);
        menuControllerstage.setTitle("Make New Crossword");
        menuControllerstage.show();
    }


    @FXML
    public void loadCrossword() {
        PlayedTiles tiles = new PlayedTiles();
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




}
