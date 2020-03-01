package All;

import OSM.Model;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


/**
 * Creates the UI of the program.
 */
public class View {

	/**
	 * Creates the UI of the program.
	 * @param model gives the correct information/data to view.
	 * @param stage creates the window frame.
	 * @throws IOException
	 */
	public View(Model model, Stage stage) throws IOException {


		FXMLLoader loader = new FXMLLoader(getClass().getResource("View.fxml"));
		Scene scene = loader.load();
		scene.getStylesheets().add("All/stylesheet.css");

			stage.setScene(scene);
			stage.setMaximized(true);
			stage.setTitle("TMap");
			stage.show();

			Controller controller = loader.getController();
			controller.init(model, stage);

	}
}
