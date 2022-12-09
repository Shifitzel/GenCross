
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

public class youwonController {
  @FXML
    Label secondsLabel;  
   @FXML 
   Label hintsLabel;
   @FXML
   Label hintsLabelSuppportingText1;    
   @FXML
   Label hintsLabelSuppportingText2; 

    @FXML
    public void returnToMenu() {
        PlayedTiles.youWonStage.close();
        MenuController.menuControllerstage.close();
    } 

    @FXML
    public void initialize() {
        secondsLabel.getParent().setStyle("-fx-background-color: b5651e");   
        secondsLabel.setText(String.valueOf(PlayedTiles.stopWatch.getElapsedTimeSecs()));
       Media sound = new Media(new File("lib/Sounds/Youwon.wav").toURI().toString()); 

        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
        if (PlayedTiles.hintAmount > 0) {
            hintsLabel.setText(String.valueOf(PlayedTiles.hintAmount));
        } else {
            hintsLabel.setVisible(false);
            hintsLabelSuppportingText1.setVisible(false);
            hintsLabelSuppportingText2.setVisible(false);
        }

    }
}
