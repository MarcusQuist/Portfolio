package All;

import Draw.Drawable;
import Draw.VisualDrawNode;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.io.Serializable;

/**
 * Makes a cartographicNode
 */
public class CartographicNode implements Drawable, Serializable {
    private float[] coord;


    public CartographicNode(float xCoord, float yCoord)
    {
        coord = new float[2];
        coord[0] = xCoord;
        coord[1] = yCoord;
    }

    public void stroke(GraphicsContext gc)
    {
    }
    public void draw(GraphicsContext gc, double zoomFactor, Image image)
    {
        // URL: https://stackoverflow.com/questions/33613664/javafx-drawimage-rotated

        new VisualDrawNode(gc,0.001-(zoomFactor*0.00004),0.00005,image, coord);


    }



    public void fill(GraphicsContext gc) {
    }

    public float[] getCoord(){
        return coord;
    }



    @Override
    public float getMaxx() {
        return coord[0];
    }

    @Override
    public float getMaxy() {
        return coord[1];
    }

    @Override
    public float getMinx() {
        return coord[0];
    }

    @Override
    public float getMiny() {
        return coord[1];
    }


    @Override
    public long getNearestIdOfNode(float lon, float lat) {
        return 0;
    }
}