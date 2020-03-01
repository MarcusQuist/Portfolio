package Draw;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


public class VisualDrawNode {
    public VisualDrawNode(GraphicsContext gc, double zoomSize, double minimumSize, Image image, float coord[]){


        if(zoomSize > minimumSize)
        {
            gc.drawImage(image, coord[0]-(zoomSize/2), coord[1],zoomSize,zoomSize);
        }
        else
        {
            gc.drawImage(image, coord[0]-(minimumSize/2), coord[1],minimumSize,minimumSize);
        }
    }
}
