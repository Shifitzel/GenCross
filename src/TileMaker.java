import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;


public class TileMaker extends Tiles {

    ArrayList<String> clues = new ArrayList<String>();
    

    

     public TileMaker() {
        super();
        Button generateCrosswordWordsButton = new Button("Generate Mini Crossword");
        generateCrosswordWordsButton.setTextFill(Color.BROWN);
        generateCrosswordWordsButton.setLayoutX(300);
        generateCrosswordWordsButton.setLayoutY(50);

        generateCrosswordWordsButton.setOnAction(a-> {
            Runtime runtime = Runtime.getRuntime();
           String exectuablePath = "lib/RandomCrosswordGenerator/main"; // Linux version

            try {
                Process process = runtime.exec(exectuablePath);
                process.waitFor();
                  File file = new File("lib/RandomCrosswordGenerator/minicrossword.txt"); 

                Scanner scanner = new Scanner(file);
                correctCrossword.clear();

                Label tempLabel = new Label();
                Rectangle tempRectangle = new Rectangle();

                for (int i = 0; i < 5; i++) {
                    correctCrossword.add(scanner.nextLine());
                    for (int t = 0; t < 5; t++) {
                        char actualvalue = correctCrossword.get(correctCrossword.size()-1).charAt(t); // "correctCrossword.get(correctCrossword.size()-1).charAt(t)" is the last char added to the list
                        list.get(t).get(i).currentValue = actualvalue; 
                        if (actualvalue != '0') {
                        list.get(t).get(i).writeable = true;
                        tempRectangle  = (Rectangle) list.get(t).get(i).stackPane.getChildren().get(0);
                        tempRectangle.setFill(Color.BLACK);
                        tempLabel = (Label) list.get(t).get(i).stackPane.getChildren().get(1);
                        tempLabel.setText(String.valueOf(actualvalue));
                         } else {
                            list.get(t).get(i).setWritable(false);
                        }
                    }
                }
                scanner.close();
            } catch(IOException e) {
                e.printStackTrace();
                System.out.println("RandomCrosswordGenerator deleted.");
            } catch (InterruptedException f) {
                f.printStackTrace();
                System.out.println("RandomCrosswordGenerator deleted.");
            }
        });

        anchorPane.getChildren().add(generateCrosswordWordsButton);


        Button generateCrossword = new Button("Generate Mini Crossword With Clues (Coming Soon)");
        generateCrossword.setTextFill(Color.BROWN);
        generateCrossword.setLayoutX(300);
        generateCrossword.setLayoutY(20);
        generateCrossword.setOnAction(e->{
            try {
                Parent root = FXMLLoader.load(getClass().getResource("FXMLs/Info.fxml")); 
                Stage stage = new Stage();
                stage.setTitle("Info");
                stage.setScene(new Scene(root));
                stage.show();  }
                catch (IOException f) {f.printStackTrace();}

        });

        anchorPane.getChildren().add(generateCrossword);
        

        for (int i = 0; i < 5; i++) {
        TextField textField = new TextField();
        textField.setLayoutX(650);
        textField.setLayoutY(70+i*50);
        clues.add("");
        textField.setOnMouseEntered(e->{
            int index = (int)((textField.getLayoutY()-70)/50);
            int otherindex = 0;
            while (!list.get(otherindex).get(index).writeable) {
            otherindex++;        
       }
       list.get(otherindex).get(index).selectTile(false,1);
        });

        textField.setOnMouseExited(e->{
         clues.set((int)(textField.getLayoutY()-70)/50,textField.getText());
        });    
        anchorPane.getChildren().add(textField); 
    }

    for (int i = 0; i < 5; i++) {
        TextField textField = new TextField();
        textField.setLayoutX(650);
        textField.setLayoutY(380+i*50);
        clues.add("");
        textField.setOnMouseEntered(e->{
            int index = (int)((textField.getLayoutY()-380)/50);
            int otherindex = 0;
            while (!list.get(index).get(otherindex).writeable) {
            otherindex++;        
       }
       list.get(index).get(otherindex).selectTile(false,2);
        });

        textField.setOnMouseExited(e->{
         clues.set((int)(textField.getLayoutY()-380)/50+5,textField.getText());
       });    
        anchorPane.getChildren().add(textField); 
    }

    scene.setOnKeyPressed(e-> {
        if (e.getCode() == KeyCode.BACK_SPACE) {
            // For the ones in the corners it checks if there is enough room to put a words that is at least three letter on both sides
            // For the ones in connected to the corners, it checks if the corners are white and if there is enough room to have a three letter word
            if (currentTile.row == 0 && currentTile.column == 0 && list.get(0).get(3).writeable && list.get(3).get(0).writeable) {
                currentTile.setWritable(false);
            } 
            else if (currentTile.row == 0 && currentTile.column == 1 && list.get(0).get(4).writeable && !list.get(0).get(0).writeable) {
                currentTile.setWritable(false);
            }
            else if (currentTile.row == 1 && currentTile.column == 0 && list.get(4).get(0).writeable && !list.get(0).get(0).writeable) {
                currentTile.setWritable(false);
            }
            else if (currentTile.row == 0 && currentTile.column == 4  && list.get(0).get(1).writeable && list.get(3).get(0).writeable) {
                currentTile.setWritable(false);
            } 
            else if (currentTile.row == 0 && currentTile.column == 3  && list.get(0).get(0).writeable && !list.get(0).get(4).writeable) {
                currentTile.setWritable(false);
            } 
            else if (currentTile.row ==1 && currentTile.column == 4  && list.get(4).get(3).writeable && !list.get(0).get(4).writeable) {
                currentTile.setWritable(false);
            } 
            else if (currentTile.row == 4 && currentTile.column == 0  && list.get(0).get(0).writeable && list.get(4).get(4).writeable) {
                currentTile.setWritable(false);
            } 
            else if (currentTile.row == 3 && currentTile.column == 0  && list.get(0).get(0).writeable && !list.get(4).get(0).writeable) {
                currentTile.setWritable(false);
            } 
            else if (currentTile.row ==4 && currentTile.column == 1  && list.get(4).get(4).writeable && !list.get(4).get(0).writeable) {
                currentTile.setWritable(false);
            } 
            else if (currentTile.row == 4 && currentTile.column == 4  && list.get(4).get(1).writeable && list.get(1).get(4).writeable) {
                currentTile.setWritable(false);
            } 
            else if (currentTile.row == 4 && currentTile.column == 3  && list.get(4).get(0).writeable && !list.get(4).get(4).writeable) {
                currentTile.setWritable(false);
            } 
            else if (currentTile.row ==3 && currentTile.column == 4  && list.get(0).get(4).writeable && !list.get(4).get(4).writeable) {
                currentTile.setWritable(false);
            } 

        }   else {
        currentTile.currentValue = keyCodeToCharacter(e.getCode()); }
        
        switchTile();
    });

    Button clearButton = new Button("Clear");
    clearButton.setLayoutX(50);
    clearButton.setLayoutY(50);
    clearButton.setOnAction(e-> {
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                list.get(r).get(c).setWritable(true);
            }
        }
    });

    anchorPane.getChildren().add(clearButton);

    Button save = new Button("Save");
    save.setLayoutX(20);   
    save.setLayoutY(20); 
    save.setOnAction(e-> {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save .mncd File");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("MNCD File","*.mncd"));
       File file = fileChooser.showSaveDialog(scene.getWindow());
       try {
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write("#MNCD\n");
        for (int r = 0; r < 5; r++) {
            for (int c= 0; c <5; c++) {
                fileWriter.write(list.get(c).get(r).currentValue);
        }
        fileWriter.write('\n');
    }
        for (int i = 0; i < 10; i++) {
            fileWriter.write(clues.get(i)+"\n");
        }
         fileWriter.close();
       } catch (IOException f) {
           f.printStackTrace();
       } 
    });


    anchorPane.getChildren().add(save);     
    
    


    }


    

   




}
