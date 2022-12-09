import java.io.FileInputStream;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;




public class MenuController {
    
    public static Stage menuControllerstage = new Stage();

    @FXML
    private ImageView logo;

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
 

    @FXML
    public void initialize() {
        logo.getParent().setStyle("-fx-background-color: b5651e");   
        try {
        logo.setImage(new Image(new FileInputStream("lib/Images/logo.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //getStylesheets().add("file:lib/StyleSheets/tilesStyle.css");

    }


}
