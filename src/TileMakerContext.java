

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class TileMakerContext {
    
    public static Stage tileMakerStage = new Stage();
    private AnchorPane anchorPane = new AnchorPane();
    public Scene scene = new Scene(anchorPane);
    private TextField textField = new TextField();

    private void makeTileMaker(int prefix, String s){
        TileMaker tiles = new TileMaker(prefix,s);
        tileMakerStage.setScene(tiles.scene);
        tileMakerStage.setTitle("Make New Crossword");
        tileMakerStage.show();
        MenuController.menuControllerstage.close();
    }

    TileMakerContext() {
        anchorPane.setPrefSize(600, 400);
        anchorPane.setStyle("-fx-background-color: b5651e");   
         Button loadButton = new Button("Load crossword");
        loadButton.setLayoutX(20);
        loadButton.setLayoutY(100);
        loadButton.setOnAction(e->{
            makeTileMaker(1,null);
        });
        anchorPane.getChildren().add(loadButton);


        textField.setLayoutX(250);
        textField.setLayoutY(200);
        anchorPane.getChildren().add(textField);

        Button enterButton = new Button("Enter");
        enterButton.setLayoutX(175);
        enterButton.setLayoutY(200);
        enterButton.setOnAction(e->{
            makeTileMaker(2,textField.getText());
        });

        anchorPane.getChildren().add(enterButton);

        Button newButton = new Button("Make New Crossword");
        newButton.setLayoutX(400);
        newButton.setLayoutY(100);
        newButton.setOnAction(e->{
            makeTileMaker(0,null);
        }); 
        anchorPane.getChildren().add(newButton); 
        scene.getStylesheets().add("file:lib/StyleSheets/tilesStyle.css");

    } 
}
