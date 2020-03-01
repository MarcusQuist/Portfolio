package Draw;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.Serializable;

/**
 * The {@code POINode} class creates a POINode, which
 * implements Drawable and Serializable. A POINode contains the
 * priamry function draw(), which draws an image on a given
 * GraphicsContext.
 *
 *
 */

public class POINode implements Drawable, Serializable {
    private float[] coord;

    public POINode(float xCoord, float yCoord)
    {
        coord = new float[2];
        coord[0] = xCoord;
        coord[1] = yCoord;
    }

    public void stroke(GraphicsContext gc)
    {
    }

    /**
     * Draws a given image, with a size according to zoomFactor on
     * the given GraphicsContext.
     * @param gc the active GraphicsContext
     * @param zoomFactor the actual zoomFactor level
     * @param image the given image to draw
     */
    public void draw(GraphicsContext gc, double zoomFactor, Image image)
    {
        new VisualDrawNode(gc, 0.008-(zoomFactor*0.0005), 0.0002, image, coord);

    }

    public void fill(GraphicsContext gc) {
    }

    public float[] getCoord(){
        return coord;
    }



    @Override
    public float getMaxx() {
        return 0;
    }

    @Override
    public float getMaxy() {
        return 0;
    }

    @Override
    public float getMinx() {
        return 0;
    }

    @Override
    public float getMiny() {
        return 0;
    }


    @Override
    public long getNearestIdOfNode(float lon, float lat) {
        return 0;
    }
}