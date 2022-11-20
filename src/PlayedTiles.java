import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.Parent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.control.Button;
import javafx.scene.control.Label;




public class PlayedTiles extends Tiles {
    public static Stage youWonStage  = new Stage();
    private Button hintButton = new Button("Hint");
    public static int hintAmount = 0;
    private int previosTime = 0;
    private boolean gameOver = false;

    protected AnimationTimer animationTimer = new AnimationTimer() { // gets the current time and update the label
        @Override
        public void handle(long arg0) {
            if (!paused) {
            timerLabel.setText(String.valueOf(stopWatch.getElapsedTimeSecs()+previosTime));
            }
        }
   };  

    private ArrayList<Label> labels = new ArrayList<Label>(); 
    private Label timerLabel = new Label("0");
    public static StopWatch stopWatch = new StopWatch(); 
  
    private void makeCrossword(String s){
        int position = 0;
        int previousPosition = 0;
        for (int i = 0; i < 5; i++) {
            position = s.indexOf((char)(87),previousPosition);
            correctCrossword.add(decodeString(s.substring(previousPosition, position),1));
            previousPosition = position+1;
            if (correctCrossword.get(i).length() != 5) {
             System.out.println("Error: not proper .mncd file.");
                return;
            }
        }
 
        for (int i = 0; i < 10; i++) {
             position = s.indexOf((char)(87),previousPosition);
            CrosswordClues.add(decodeString(s.substring(previousPosition, position),1));
            previousPosition = position+1;
        }
    }


     public PlayedTiles(int prefix, String word) {
       super();
       if (prefix == 1) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load .mncd file");
            fileChooser.getExtensionFilters().add(new ExtensionFilter("MNCD file","*.mncd"));
        File file = fileChooser.showOpenDialog(scene.getWindow());
        try {
        Scanner scanner = new Scanner(file);
            if (scanner.next().hashCode() != "#MNCD".hashCode()) { 
                System.out.println("Error: not proper .mncd file.");
                scanner.close();
                return;
            }
            String s = scanner.next();
            scanner.close();
            makeCrossword(s);
        }
        catch(IOException e) {
            System.out.println("file deleted.");
        }  
    } else if (prefix == 2) {
        if (word.substring(0,word.indexOf(' ')).hashCode() != "#MNCD".hashCode()) { 
            System.out.println("Error: not proper .mncd file.");
            return;
        }
        makeCrossword(word.substring(word.indexOf(' ')+1));
    }

        for (int r = 0; r < 5; r++) {
            for (int c  = 0; c < 5; c++) {
                if (correctCrossword.get(c).charAt(r) == '0') { // if the character is an '0' than it repersents a white space
                    list.get(r).get(c).setWritable(false);
                }
            }
        } 

        for (int i = 0; i < 5; i++) {

            Label label = new Label(CrosswordClues.get(i));
            label.setLayoutX(800);
           label.setLayoutY(-20+i*50);
           label.setPrefSize(200, 200);
           label.setFont(Font.font("Times New Roman", 15));
           label.setTextFill(Color.BLACK);
           label.setWrapText(true);
            labels.add(label);
           anchorPane.getChildren().add(label);  
        }
        for (int i = 0; i < 5; i++) { // I had to make a seperate label for the hitboxes becuase the original label's hitboxes were acting weird
            Label hitboxLabel = new Label("");
            hitboxLabel.setLayoutX(800);
            hitboxLabel.setLayoutY(70+i*50);
            hitboxLabel.setPrefWidth(200);
            hitboxLabel.setPrefHeight(10);
 
            hitboxLabel.setOnMouseEntered(e->{
                int index = (int)((hitboxLabel.getLayoutY()-70)/50);
                labels.get(index).setTextFill(Color.BLUE); 
            });
            hitboxLabel.setOnMouseExited(e->{
                int index = (int)((hitboxLabel.getLayoutY()-70)/50);
                labels.get(index).setTextFill(Color.BLACK); 
            });
 
             hitboxLabel.setOnMousePressed( e-> {
                int index = (int)((hitboxLabel.getLayoutY()-70)/50);
                int otherindex = 0;
                while (!list.get(otherindex).get(index).writeable) {
                otherindex++;        
           }
           list.get(otherindex).get(index).selectTile(false,1);
                 }
             );
             anchorPane.getChildren().add(hitboxLabel); 
        }

        for (int i = 0; i < 5; i++) {
            Label label = new Label(CrosswordClues.get(i+5));
            label.setLayoutX(800);
           label.setLayoutY(290+i*50);
           label.setPrefSize(200, 200);
           label.setFont(Font.font("Times New Roman", 15));
           label.setTextFill(Color.BLACK);
           labels.add(label);
           anchorPane.getChildren().add(label);  
        }

        for (int i = 0; i < 5; i++){
            Label hitboxLabel = new Label("");
            hitboxLabel.setLayoutX(800);
            hitboxLabel.setLayoutY(380+i*50);
            hitboxLabel.setPrefWidth(200);
            hitboxLabel.setPrefHeight(10);
            hitboxLabel.setOnMouseEntered(e->{
             int index = (int)((hitboxLabel.getLayoutY()-130)/50);
             labels.get(index).setTextFill(Color.BLUE); 
         });
         hitboxLabel.setOnMouseExited(e->{
             int index = (int)((hitboxLabel.getLayoutY()-130)/50);
             labels.get(index).setTextFill(Color.BLACK); 
         });
 
          hitboxLabel.setOnMousePressed( e-> {
            int index = (int)((hitboxLabel.getLayoutY()-130)/50);
            int otherindex = 0;
            while (!list.get(index-5).get(otherindex).writeable) {
            otherindex++;        
       }
       list.get(index-5).get(otherindex).selectTile(false,2);
          }
          );
             anchorPane.getChildren().add(hitboxLabel);
        }

                timerLabel.setLayoutX(450);
       timerLabel.setLayoutY(-50);
        timerLabel.setPrefSize(200, 200);
        timerLabel.setTextFill(Color.RED);
        anchorPane.getChildren().add(timerLabel);

     scene.setOnKeyPressed( e-> {
         if (!paused) {
        currentTile.currentValue = keyCodeToCharacter(e.getCode());
        if (currentTile.currentValue != ' ' ) {
            switchTile();
        } else {
            Label label = (Label)(currentTile.stackPane.getChildren().get(1));
            label.setText(" ");
        }
        IsCorrectBoard();
         }
     });

        hintButton.setLayoutX(20);
        hintButton.setLayoutY(50);
        hintButton.setOnAction(e->{
            int firstTile = 0;
            if (currentTile != null) {
                hintAmount++;
            if (RoworColumn) { // if yes fill out the row, if not fill out the column
                while (!list.get(firstTile).get(currentTile.row).writeable) { // increments the firstile until it lands on a writable tile, same for column
                    firstTile++;
                }
                currentTile = list.get(firstTile).get(currentTile.row);
                while (currentTile.column != 4 && list.get(currentTile.column+1).get(currentTile.row).writeable) {
                    currentTile.currentValue = correctCrossword.get(currentTile.row).charAt(currentTile.column);
                    switchTile(); 
                }
                if (list.get(currentTile.column).get(currentTile.row).writeable) {
                  currentTile.currentValue = correctCrossword.get(currentTile.row).charAt(currentTile.column);
                    switchTile();
                }


            }  else {
                while (!list.get(currentTile.column).get(firstTile).writeable) {
                    firstTile++;
                }
                currentTile = list.get(currentTile.column).get(firstTile);
                while (currentTile.row != 4 && list.get(currentTile.column).get(currentTile.row+1).writeable ) {
                    currentTile.currentValue = correctCrossword.get(currentTile.row).charAt(currentTile.column);
                    switchTile();
             }
             if (list.get(currentTile.column).get(currentTile.row).writeable) {
                currentTile.currentValue = correctCrossword.get(currentTile.row).charAt(currentTile.column);
                switchTile();
             }
            } 
         } 
         IsCorrectBoard();
        
        });
        anchorPane.getChildren().add(hintButton);

        Button pauseButton = new Button("Pause");
        pauseButton.setLayoutY(50);
        pauseButton.setLayoutX(70);
        pauseButton.setOnMouseClicked(e->{
          if (!gameOver) {  
            if (paused) {
                paused = false;
                pauseButton.setText("Pause");
                stopWatch.start();
                for (int i = 0; i < 20; i++) {
                    Label label = (Label) anchorPane.getChildren().get(anchorPane.getChildren().size()-4-i);
                    label.setVisible(true);
                }
            } else  {
            paused = true; 
            pauseButton.setText("Play");    
            previosTime += stopWatch.getElapsedTimeSecs();
            stopWatch.stop();
                for (int i = 0; i < 20; i++) {
                    Label label = (Label) anchorPane.getChildren().get(anchorPane.getChildren().size()-4-i);
                    label.setVisible(false);
                }
            }    
        }
    });
        

        anchorPane.getChildren().add(pauseButton);

       animationTimer.start();
       stopWatch.start();
    }


 

    public void IsCorrectBoard() {
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                Tile tile = (Tile) list.get(c).get(r);
                if (tile.writeable) {
                    if (tile.currentValue == null) {
                        return;
                    }
                    if (tile.currentValue != correctCrossword.get(r).charAt(c)) {
                        return;
                    }

                }

            }
        }
        // If it didn't return than the board is filled out correctly and it loads the "You Won" screen.
        animationTimer.stop();

        Parent youWonRoot = new Parent() {            
        };
        try {
         youWonRoot = FXMLLoader.load(getClass().getResource("FXMLs/Youwon.fxml")); 
        } catch(IOException e) {e.printStackTrace();}
        Scene youWonScene = new Scene(youWonRoot);
        youWonStage.setScene(youWonScene);
        youWonStage.show();  
        gameOver = true;
        paused = true;  
    }  

       



  

 

   


}
