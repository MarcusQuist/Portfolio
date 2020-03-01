package All;

import OSM.Model;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Changes the currently loaded mapfile
 */

public class ChangeFile {
    private FileChooser filechooser;
    private Model model;

    /**
     * Initialises filechooser with extensionfilter
     */
    public ChangeFile(){

        this.filechooser = new FileChooser();
        filechooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("OSM Files", "*.osm"),
                new FileChooser.ExtensionFilter("OBJ Files", "*.osm.obj"),
                new FileChooser.ExtensionFilter("ZIP Files", "*.zip")
        );
    }

    /**
     * Changes the currently loaded file to the file the user pressed in the filechooser menu
     * @param stage Current stage
     * @return A new model generated from the new file
     * @throws IOException Exception when reading file
     * @throws XMLStreamException Exception from xml file
     * @throws ClassNotFoundException Exception if class could not be found
     */
    public Model pickFile(Stage stage) throws XMLStreamException, IOException, ClassNotFoundException {
        File selectedfile = filechooser.showOpenDialog(null);
        if (selectedfile != null) {
            URL url = selectedfile.toURI().toURL();
            InputStream is = url.openStream();
            String filename = url.getFile();
            model = new Model(filename, is);
            updateView(stage);
        }
        return model;
    }

    /**
     * Creates a new view with the new model
     * @param stage Current stage (window)
     * @throws IOException Exception when reading file
     */
    private void updateView(Stage stage) throws IOException {
       new View(model, stage);
    }
}








