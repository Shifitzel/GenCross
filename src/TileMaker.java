import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;


public class TileMaker extends Tiles {
    
    private Timer timer = new Timer();
    private Clipboard clipboard = Clipboard.getSystemClipboard(); 
    public static Stage addWordsStage  = new Stage();
    ArrayList<String> clues = new ArrayList<String>();
    private Process process;
    private CheckBox ignoreCrossword = new CheckBox("Ignore Crossword");
    private CheckBox onlyUserWords = new CheckBox("Only User Words");
    private ImageView loadingScene = new ImageView(); 
    private int textFieldStartNumber = 0;
    private Button endProcess = new Button("End Process");
    private  Label copyIndicator = new Label("Copied");



    public void makeCrossword(String s) {
        int position = 0;
        int previousPosition = 0;

       for (int i = 0; i < 5; i++) {
           position = s.indexOf((char)(87),previousPosition);
           correctCrossword.add(decodeString(s.substring(previousPosition, position),1));
           previousPosition = position+1;
           for (int t = 0; t < 5; t++) {
               char actualvalue = correctCrossword.get(correctCrossword.size()-1).charAt(t); // "correctCrossword.get(correctCrossword.size()-1).charAt(t)" is the last char added to the list
               list.get(t).get(i).currentValue = actualvalue; 
               if (actualvalue != '0') {
               list.get(t).get(i).writeable = true;
               Rectangle tempRectangle;
               Label tempLabel;
               tempRectangle  = (Rectangle) list.get(t).get(i).stackPane.getChildren().get(0);
               tempRectangle.setFill(Color.BLACK);
               tempLabel = (Label) list.get(t).get(i).stackPane.getChildren().get(1);
               tempLabel.setText(String.valueOf(actualvalue));
                } else {
                   list.get(t).get(i).setWritable(false);
               }
           }
           
           if (correctCrossword.get(i).length() != 5) {
            System.out.println("Error: not proper .mncd file.");
               return;
           }
       }

       for (int i = 0; i < 10; i++) {
           position = s.indexOf((char)(87),previousPosition);
           clues.add(decodeString(s.substring(previousPosition, position),1));
           previousPosition = position+1;
       }
    }

    public void loadMinicrossword() {
        Platform.runLater(new Runnable() {
  
            public void run() {
                File file = new File("lib/RandomCrosswordGenerator/minicrossword.txt"); 

                try {
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
                
                for (int i = 0; i < 10; i++) {
                    clues.set(i, scanner.nextLine());
                    TextField textField = (TextField)(anchorPane.getChildren().get(textFieldStartNumber+i));
                    if (clues.get(i).hashCode() != "".hashCode()) {
                    textField.setText(clues.get(i));
                    }
                }
                scanner.close();
                setSigns();
            } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }  
                showLoadingScreen(false,false, null);
            }

        }
        );


    }   

    public void showLoadingScreen(boolean showImage, boolean showButton, String imageSource) {
        loadingScene.setVisible(showImage);
        if (imageSource != null) {
            try {
            loadingScene.setImage(new Image(new FileInputStream(imageSource)));
            } catch (IOException f) {
                f.printStackTrace();
            }  
        }

        endProcess.setVisible(showButton);
        endProcess.setDisable(!showButton);
        
    } 

    public void generateMiniCrossword() {
        if (onlyUserWords.isSelected()) {
            boolean cont = false;
            boolean fiveLetterString = false;
            boolean threeLetterString = false;
            boolean fourLetterString = false;
            try {
                Scanner scanner = new Scanner(new File("lib/RandomCrosswordGenerator/otherWords.txt"));
                while (scanner.hasNextLine()) {
                switch (scanner.nextLine().indexOf('@')) {
                    case 3: threeLetterString = true; break;
                    case 4: fourLetterString = true; break;
                    case 5: fiveLetterString = true; break;
                } 
            }
                scanner.close();
            } catch (IOException f) {
                f.printStackTrace();
            }
            if (!fiveLetterString) {
                showLoadingScreen(true, false, "lib/Images/Error1.png");
                cont = true;
            }

            if (!ignoreCrossword.isSelected() && !cont) {
                int whitesPerSection = 0;
                for (int r = 0; r < 5; r++) {
                    for (int c = 0; c < 5; c++) {
                        if (!list.get(r).get(c).writeable) {
                            whitesPerSection++;
                        }
                    }
                    if (whitesPerSection == 1 || whitesPerSection == 2) {
                        break;
                    }
                    whitesPerSection = 0;
                }
                if (whitesPerSection != 2) {
                whitesPerSection = 0;
                for (int r = 0; r < 5; r++) {
                    for (int c = 0; c < 5; c++) {
                        if (!list.get(c).get(r).writeable) {
                            whitesPerSection++;
                        }
                    }
                    if (whitesPerSection == 1 || whitesPerSection == 2) {
                        break;
                    }
                    whitesPerSection = 0;
                }
            }

             if ( whitesPerSection == 1 && !fourLetterString ) {
                    showLoadingScreen(true, false, "lib/Images/Error2.png");
                    cont = true;
            } else if ( whitesPerSection == 2 && !threeLetterString ) {
                    showLoadingScreen(true, false, "lib/Images/Error3.png");
                    cont = true;
              } 
            } 
        

            if (cont) {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    public void run() {
                        showLoadingScreen(false, false, null);
                    }
                },2000);
                return;
            }
        }

        Runtime runtime = Runtime.getRuntime();
        String exectuablePath = null;
        if (Main.oS == "Linux") {
        exectuablePath = "lib/RandomCrosswordGenerator/main"; 
        } else if (Main.oS == "Windows") {
            exectuablePath = "lib/RandomCrosswordGenerator/main.exe";
        }

        try {
            
            FileWriter fileWriter = new FileWriter("lib/RandomCrosswordGenerator/input.txt");


            fileWriter.write(ignoreCrossword.isSelected() ? "0" : "1");
            fileWriter.write(" ");
            fileWriter.write( onlyUserWords.isSelected() ? "1" : "0");
            fileWriter.write("\n");
            for (int r = 0; r < 5; r++) {
                for (int c= 0; c <5; c++) {
                    if (!ignoreCrossword.isSelected()) {
                        fileWriter.write(list.get(c).get(r).currentValue);
                    } else {
                        fileWriter.write(' ');
                    }
            }
            fileWriter.write('\n');
        }
            fileWriter.close();

             process = runtime.exec(exectuablePath);
             process.getOutputStream().write(System.getProperty("user.dir").getBytes()); // tells the generator what the absolute path is
                process.getOutputStream().close();



            
           process.onExit().thenAccept( res->{loadMinicrossword(); });
            
           // loadMinicrossword();
            
        
            
        } catch(IOException e) {
            e.printStackTrace();
            System.out.println("RandomCrosswordGenerator deleted.");
        } 

        
    }
    

     public TileMaker(int prefix, String word) {
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

        Button generateCrosswordWordsButton = new Button("Generate Mini Crossword");


        generateCrosswordWordsButton.setTextFill(Color.BROWN);
        generateCrosswordWordsButton.setLayoutX(300);
        generateCrosswordWordsButton.setLayoutY(20);

        generateCrosswordWordsButton.setOnAction(a-> {
            showLoadingScreen(true,true, null);
    
         
                generateMiniCrossword();
        
        });

        anchorPane.getChildren().add(generateCrosswordWordsButton);


        Button generateCrossword = new Button("Generate Mini Crossword With Clues (Coming Soon)");
        generateCrossword.setTextFill(Color.BROWN);
        generateCrossword.setLayoutX(300);
        generateCrossword.setLayoutY(50);
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
        
        try {
       loadingScene.setImage(new Image(new FileInputStream("lib/Images/loadingScreen.gif")));
       loadingScene.setLayoutY(100);
       loadingScene.setLayoutX(200);
       loadingScene.setVisible(false);
       anchorPane.getChildren().add(loadingScene);
    }
       catch (FileNotFoundException e) {
        e.printStackTrace();
       }

       endProcess.setLayoutX(400);
       endProcess.setLayoutY(350);
       endProcess.setOnAction(e->{
        process.destroy();
        showLoadingScreen(false,false, null);
       }); 
       endProcess.setDisable(true);
       endProcess.setVisible(false);

       anchorPane.getChildren().add(endProcess); 
        

       textFieldStartNumber = anchorPane.getChildren().size();
    for (int i = 0; i < 5; i++) {
        TextField textField = new TextField();
        if (clues.size() > i) {
            textField.setText(clues.get(i));
        }
        textField.setLayoutX(800);
        textField.setLayoutY(70+i*50);
        clues.add("");
        textField.setOnMouseClicked(e->{
            int index = (int)((textField.getLayoutY()-70)/50);
            int otherindex = 0;
            while (!list.get(otherindex).get(index).writeable) {
            otherindex++;        
       }
       list.get(otherindex).get(index).selectTile(false,1);
        });

 
        textField.setOnKeyReleased(e->{
            clues.set((int)(textField.getLayoutY()-70)/50,textField.getText());
        });

        anchorPane.getChildren().add(textField); 
    }

    for (int i = 0; i < 5; i++) {
        TextField textField = new TextField();
        if (clues.size() > i+5) {
            textField.setText(clues.get(i+5));
        }
        textField.setLayoutX(800);
        textField.setLayoutY(380+i*50);
        clues.add("");
        textField.setOnMouseClicked(e->{
            int index = (int)((textField.getLayoutY()-380)/50);
            int otherindex = 0;
            while (!list.get(index).get(otherindex).writeable) {
            otherindex++;        
       }
       list.get(index).get(otherindex).selectTile(false,2);
        });

        
        textField.setOnKeyReleased(e->{
         clues.set((int)(textField.getLayoutY()-380)/50+5,textField.getText());
       });    
        anchorPane.getChildren().add(textField); 
    }

    scene.setOnKeyPressed(e-> {
       
        if (e.getCode() == KeyCode.BACK_SPACE && currentTile.currentValue == ' ') {
            if (!currentTile.writeable ){
                if ( (currentTile.column == 0 && currentTile.row == 0) || (currentTile.column == 0 && currentTile.row == 4) || (currentTile.column == 4 && currentTile.row == 4) || (currentTile.column == 4 && currentTile.row == 0) ) {
                    if (currentTile.column == 0 && currentTile.row == 0 && list.get(0).get(1).writeable && list.get(1).get(0).writeable) {
                    currentTile.setWritable(true);
                    } else if (currentTile.column == 0 && currentTile.row == 4 && list.get(1).get(4).writeable && list.get(0).get(3).writeable) {
                        currentTile.setWritable(true);
                    } else if (currentTile.column == 4 && currentTile.row == 4 && list.get(4).get(3).writeable && list.get(3).get(4).writeable) {
                        currentTile.setWritable(true);
                    } else if (currentTile.column == 4 && currentTile.row == 0 && list.get(4).get(1).writeable && list.get(1).get(4).writeable) {
                        currentTile.setWritable(true);
                    } 
                } else {
                    currentTile.setWritable(true);
                }
                addLetterToColumn(currentTile.column);
                addNumberToRow(currentTile.row);
            } else {

            // For the ones in the corners it checks if there is enough room to put a words that is at least three letter on both sides
            // For the ones in connected to the corners, it checks if the corners are white and if there is enough room to have a three letter word
            if (currentTile.column == 0 && currentTile.row == 0 && list.get(0).get(3).writeable && list.get(3).get(0).writeable) {
                currentTile.setWritable(false);
            } 
            else if (currentTile.column == 0 && currentTile.row == 1 && list.get(0).get(4).writeable && !list.get(0).get(0).writeable) {
                currentTile.setWritable(false);
            }
            else if (currentTile.column == 1 && currentTile.row == 0 && list.get(4).get(0).writeable && !list.get(0).get(0).writeable) {
                currentTile.setWritable(false);
            }
            else if (currentTile.column == 0 && currentTile.row == 4  && list.get(0).get(1).writeable && list.get(3).get(4).writeable) {
                currentTile.setWritable(false);
            } 
            else if (currentTile.column == 0 && currentTile.row == 3  && list.get(0).get(0).writeable && !list.get(0).get(4).writeable) {
                currentTile.setWritable(false);
            } 
            else if (currentTile.column ==1 && currentTile.row == 4  && list.get(4).get(3).writeable && !list.get(0).get(4).writeable) {
                currentTile.setWritable(false);
            } 
            else if (currentTile.column == 4 && currentTile.row == 0  && list.get(1).get(0).writeable && list.get(4).get(3).writeable) {
                currentTile.setWritable(false);
            } 
            else if (currentTile.column == 3 && currentTile.row == 0  && list.get(0).get(0).writeable && !list.get(4).get(0).writeable) {
                currentTile.setWritable(false);
            } 
            else if (currentTile.column ==4 && currentTile.row == 1  && list.get(4).get(4).writeable && !list.get(4).get(0).writeable) {
                currentTile.setWritable(false);
            } 
            else if (currentTile.column == 4 && currentTile.row == 4  && list.get(4).get(1).writeable && list.get(1).get(4).writeable) {
                currentTile.setWritable(false);
            } 
            else if (currentTile.column == 4 && currentTile.row == 3  && list.get(4).get(0).writeable && !list.get(4).get(4).writeable) {
                currentTile.setWritable(false);
            } 
            else if (currentTile.column ==3 && currentTile.row == 4  && list.get(0).get(4).writeable && !list.get(4).get(4).writeable) {
                currentTile.setWritable(false);
            } 
            addLetterToColumn(currentTile.column);
            addNumberToRow(currentTile.row);
            switchTile();
        } 
      }  else {
        currentTile.currentValue = keyCodeToCharacter(e.getCode()); 
        if (currentTile.currentValue == ' ') {
            Label label = (Label)(currentTile.stackPane.getChildren().get(1));
            label.setText(" ");
        } else {
            switchTile();
        }
    }
    
        
    });

    Button clearButton = new Button("Clear");
    clearButton.setShape(new Rectangle(50,50));   
    clearButton.setLayoutX(20);
    clearButton.setLayoutY(50);
    clearButton.setOnAction(e-> {
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                list.get(r).get(c).setWritable(true);
            }
        }
        setSigns();
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
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("#MNCD ");
        for (int r = 0; r < 5; r++) {
            for (int c= 0; c <5; c++) {
                stringBuilder.append((char) (list.get(c).get(r).currentValue+'a'));
        }
        stringBuilder.append((char)(87) );
    }
        for (int i = 0; i < 10; i++) {

            stringBuilder.append(decodeString(clues.get(i),-1));
            stringBuilder.append((char)(87));
        }
            fileWriter.write(stringBuilder.toString());
         fileWriter.close();
       } catch (IOException f) {
           f.printStackTrace();
       } 
    });

    


    anchorPane.getChildren().add(save);     
    
    
    Button addWordsButton = new Button("Add words");
    addWordsButton.setLayoutX(20);
    addWordsButton.setLayoutY(110);

    addWordsButton.setOnAction(e->{
       AddWords addWords = new AddWords();
       addWordsStage.setScene(addWords.scene);
       addWordsStage.setTitle("Add Words");
       addWordsStage.show();


    });

    anchorPane.getChildren().add(addWordsButton);

    ignoreCrossword.setLayoutX(490);
    ignoreCrossword.setLayoutY(22.5);
    anchorPane.getChildren().add(ignoreCrossword);

    onlyUserWords.setLayoutX(650);
    onlyUserWords.setLayoutY(22.5);
    anchorPane.getChildren().add(onlyUserWords);

    Button copyButton = new Button("Copy");
    copyButton.setLayoutX(20);
    copyButton.setLayoutY(80);
    copyButton.setOnAction(e->{
        ClipboardContent clipboardcontent = new ClipboardContent();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("#MNCD ");
        for (int r = 0; r < 5; r++) {
            for (int c= 0; c <5; c++) {
                stringBuilder.append(String.valueOf ((char) (list.get(c).get(r).currentValue+'a') ));
        }
        stringBuilder.append((char)(87));
    }
        for (int i = 0; i < 10; i++) {
            stringBuilder.append(decodeString(clues.get(i),-1));
            stringBuilder.append((char)(87));
        }
        clipboardcontent.putString(stringBuilder.toString());
        clipboard.setContent(clipboardcontent); 
        copyIndicator.setVisible(true);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                copyIndicator.setVisible(false);
            }
        }, 500);
    
    });

    anchorPane.getChildren().add(copyButton);

    copyIndicator.setLayoutX(80);
     copyIndicator.setLayoutY(85);
     copyIndicator.setVisible(false);

     anchorPane.getChildren().add(copyIndicator);

    }






    

   




}
