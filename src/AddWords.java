import javafx.geometry.Orientation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class AddWords{


   private AnchorPane anchorPane = new AnchorPane();
   private TreeSet<String> words = new TreeSet<String>();
    public Scene scene = new Scene(anchorPane,600,800);
    private VBox vBox = new VBox();// must always be last
    private ScrollBar scrollBar = new ScrollBar();
    private Button deleteButton = new Button("X");
    private Timer timer = new Timer();
    private String selectedWord = new String();
    private TextField cluesTextField = new TextField("Enter Clue");
    private TextField wordsTextField = new TextField("Enter Word");
    private Line vLine = new Line(0, 0, 0, 500000);


    private void scroll(ObservableValue<? extends Number> ov,
    Number old_val, Number new_val) {
        double vBoxY = vBox.getLayoutY();
        for (int i = 0; i < anchorPane.getChildren().size(); i++) {
            if (anchorPane.getChildren().get(i) != scrollBar ){
                if (i != 0 ) {
                   double LayoutY = anchorPane.getChildren().get(i).getLayoutY();
            anchorPane.getChildren().get(i).setLayoutY(-new_val.doubleValue()+LayoutY-vBoxY);
                } else {
                    anchorPane.getChildren().get(i).setLayoutY(-new_val.doubleValue());
                }
        }
    }
    }




    private void addWordToDisplay(String word){
        HBox hBox = new HBox();

        Label label = new Label(word.substring(0,word.indexOf('@')));
        Label clueLabel = new Label(word.substring(word.indexOf('@')+1));
        clueLabel.setPrefWidth(200);
        clueLabel.setWrapText(true);

        label.setOnMouseEntered(e->{
            timer.cancel();
            label.setTextFill(Color.BLUE);
            selectedWord = label.getText() + "@" + clueLabel.getText();
            wordsTextField.setText(label.getText());
            cluesTextField.setText(clueLabel.getText());
            deleteButton.setLayoutX(250);
            deleteButton.setLayoutY( hBox.localToScene(0,0).getY() );
            deleteButton.setVisible(true);
            
        });
        label.setOnMouseExited(e->{
            label.setTextFill(Color.BLACK);
            timer = new Timer();
            timer.schedule(new TimerTask() {
               public void run() {
                selectedWord = "";
                deleteButton.setVisible(false);
                }
            }, 1000);
        });

        hBox.getChildren().add(label);
        hBox.setSpacing(127-label.getText().length()*9); 

        clueLabel.setTextFill(Color.BLACK);
        hBox.getChildren().add(clueLabel);


        vBox.getChildren().add(hBox);

    }

    private void displayWords(){
        boolean difference = false;
        int i = 0;
        for (String s: words) {
            
            if (i >= vBox.getChildren().size()) {
                addWordToDisplay(s);
            } else {
                HBox hBox = (HBox) vBox.getChildren().get(i);
                    Label label = (Label) hBox.getChildren().get(0);
                    Label cluesLabel = (Label) hBox.getChildren().get(1);
                if (!difference) {
                    if (s != label.getText()+"@"+cluesLabel.getText()) {
                        difference = true;
                    }
                } 
                if (difference) {
                    label.setText(s.substring(0,s.indexOf('@')));
                    hBox.setSpacing(127-9*label.getText().length());
                    cluesLabel.setText(s.substring(s.indexOf('@')+1));
                }
            }
            i++;
        }

        while (words.size() != vBox.getChildren().size()){
            vBox.getChildren().remove(vBox.getChildren().size()-1);
        }

    }

    public void remove(String s) {
        words.remove(s);
        displayWords();
    }

    public void add(String word, String clue) {
        if (word.indexOf(' ') == -1 && word.length() <= 5 && word.length() >= 3) {
            words.add(word+"@"+clue);
            displayWords();
        }

    }

    public AddWords() {
        vBox.setLayoutY(150);
        vBox.setLayoutX(200);
        vBox.setSpacing(20);
        anchorPane.setStyle("-fx-background-color: brown");   
        anchorPane.getChildren().add(vBox); 
 
        try {
            Scanner scanner = new Scanner(new File("lib/RandomCrosswordGenerator/otherWords.txt"));
            String word = new String();
            while(scanner.hasNextLine()) {
                word = scanner.nextLine();
                words.add(word);                
                addWordToDisplay(word);

            }
            scrollBar.setLayoutX(scene.getWidth()-scrollBar.getWidth());
            scrollBar.setLayoutY(350);
            scrollBar.setMin(-150);
            scrollBar.setOrientation(Orientation.VERTICAL);
            scrollBar.setPrefHeight(180);
            scrollBar.setMax(words.size()*36+scrollBar.getMin());

            scrollBar.valueProperty().addListener(new ChangeListener<Number>() {
                public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                        scroll(ov,old_val,new_val);
                }
            });
    
            anchorPane.getChildren().add(scrollBar);


            deleteButton.setVisible(false);
            deleteButton.setOnAction(e->{
                remove(selectedWord);
            });
            anchorPane.getChildren().add(deleteButton);

              Label addWords = new Label("Add Words");
            addWords.setScaleX(2);
            addWords.setScaleY(2);
            addWords.setLayoutX(250);
            addWords.setLayoutY(50);
            anchorPane.getChildren().add(addWords);

            wordsTextField.setLayoutY(15);
            wordsTextField.setLayoutX(25);
            wordsTextField.setOnAction(e->{
                add(wordsTextField.getText(), cluesTextField.getText());
            } );

            cluesTextField.setLayoutY(85);
            cluesTextField.setLayoutX(25);
            cluesTextField.setOnAction(e->{
                add(wordsTextField.getText(), cluesTextField.getText());
            } );

            anchorPane.getChildren().add(wordsTextField);
            anchorPane.getChildren().add(cluesTextField);

            Button addWordButton = new Button("Add");
            addWordButton.setLayoutY(50);
            addWordButton.setLayoutX(25);
            addWordButton.setOnAction(e->{
                add(wordsTextField.getText().toUpperCase(), cluesTextField.getText());
            });    
            anchorPane.getChildren().add(addWordButton);

            Button clearButton = new Button("Clear");
            clearButton.setLayoutY(50);
            clearButton.setLayoutX(140);
            clearButton.setOnAction(e->{
                words.clear();
                vBox.getChildren().clear();
                displayWords();
            }); 
            anchorPane.getChildren().add(clearButton);


            Button saveAndExitButton = new Button("Save and Exit   ");
            saveAndExitButton.setLayoutY(50);
            saveAndExitButton.setLayoutX(450);
            saveAndExitButton.setOnAction(e->{
                try {
                    FileWriter fileWriter = new FileWriter("lib/RandomCrosswordGenerator/otherWords.txt");
                    for (String s: words) {
                        fileWriter.write(s+"\n");
                    }
                    fileWriter.close();
                    Stage stage = (Stage) scene.getWindow();
                    stage.close();
                } catch (IOException f) {
                    System.out.println("otherWords.txt deleted");
                }
            });

            anchorPane.getChildren().add(saveAndExitButton);

            Button saveToFileButton = new Button("Save to File      ");
            saveToFileButton.setLayoutY(25);
            saveToFileButton.setLayoutX(450);
            saveToFileButton.setOnAction(e->{
                
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("save to .words file");
                fileChooser.getExtensionFilters().add(new ExtensionFilter("WORDS file","*.words"));
               File file = fileChooser.showSaveDialog(scene.getWindow());
               try {
               FileWriter fileWriter = new FileWriter(file);
               for (String s: words) {
                    fileWriter.write(s+"\n");
                }
                fileWriter.close();

                } catch (IOException f) {
                    f.printStackTrace();
                }
            });

            anchorPane.getChildren().add(saveToFileButton);


        } catch (IOException e) {
            System.out.println("otherWords.txt deleted");
            
        }

        Button loadWordsButton = new Button("Load .words file");
        loadWordsButton.setLayoutY(75);
        loadWordsButton.setLayoutX(450);
        loadWordsButton.setOnAction(e->{
            try{
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Load .words File");
                fileChooser.getExtensionFilters().add(new ExtensionFilter("WORDS file","*.words"));
               File file = fileChooser.showOpenDialog(scene.getWindow());
            Scanner scanner = new Scanner(file);
            String word = new String();
            while(scanner.hasNextLine()) {
                words.clear();
                vBox.getChildren().clear();
                word = scanner.nextLine();
                words.add(word);                
                addWordToDisplay(word);

            }
            scrollBar.setMax(words.size()*36+scrollBar.getMin());
            scanner.close();
        } catch (IOException f) {
            f.printStackTrace();
        }
        });

        anchorPane.getChildren().add(loadWordsButton);

        vLine.setLayoutX(300);
        vLine.setLayoutY(125);
        anchorPane.getChildren().add(vLine);

        Line line2 = new Line(0, 0, 10000, 0);
        line2.setLayoutX(0);
        line2.setLayoutY(125);
        anchorPane.getChildren().add(line2);

        scrollBar.setUnitIncrement(10);     
        scene.setOnScroll(e->{
            scrollBar.unitIncrementProperty().set(e.getDeltaY());
            scrollBar.increment();
    });

    }

}