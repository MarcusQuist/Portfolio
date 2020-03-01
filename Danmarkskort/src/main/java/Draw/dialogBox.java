package Draw;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Different dialogboxes such as errorbox, confirmationbox.
 */
public class dialogBox {

    public dialogBox(){}

    public void errorBox(String headerText, String contentText){
        Alert errorBox = new Alert(Alert.AlertType.ERROR);
        errorBox.setTitle("Error");
        if (!headerText.equals("")){
            errorBox.setHeaderText(headerText);
        }
        errorBox.setContentText(contentText);
        errorBox.showAndWait();
    }

    public boolean confirmationBox( String contentText){
        Alert confirmationBox = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationBox.setTitle("Confirmation");
        confirmationBox.setContentText(contentText);

        ButtonType buttonYes = new ButtonType("Yes");
        ButtonType buttonNo = new ButtonType("No");
        confirmationBox.getButtonTypes().setAll(buttonYes, buttonNo);
        Optional<ButtonType> result = confirmationBox.showAndWait();
        if (result.get() == buttonYes){
            return true;
        }else if (result.get() == buttonNo){
            return false;
        }
        else {
            return false; }
    }
}
