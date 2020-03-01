package Draw;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Drawable Interface
 */
public interface Drawable{
	public void stroke(GraphicsContext gc);
	public void fill(GraphicsContext gc);
	public void draw(GraphicsContext gc, double zoomFactor, Image image);

	float[] getCoord();

	public float getMaxx();

	public float getMaxy();

	public float getMinx();

	public float getMiny();

	public long getNearestIdOfNode(float lon, float lat);

}
