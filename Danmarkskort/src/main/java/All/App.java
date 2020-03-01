package All;

import OSM.Model;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.InputStream;

/**
 * Executes the program
 */

public class App extends Application {
	private Model model;

	@Override
	public void start(Stage stage) throws Exception {
		//IS CAN RUN THE MAP DENMARK AS OSM AND OBJ. THE JAR RUNS DENMARK.

		String urlString = "/All/denmark-latest.osm.obj";
		InputStream inputStream = App.class.getResourceAsStream(urlString);
		model = new Model(urlString, inputStream);
		View view = new View(model, stage);
	}
	public Model getModel()
	{
		return model;
	}
}

