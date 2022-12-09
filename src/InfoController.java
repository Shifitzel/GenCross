import java.net.URI;
import java.net.URISyntaxException;

import javafx.fxml.FXML;

import java.awt.Desktop;
import java.io.IOException;

public class InfoController {

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
