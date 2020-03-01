package OSM;
import Draw.Drawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.io.Serializable;
import java.util.ArrayList;
public class MultiPolyline extends ArrayList<Polyline> implements Drawable, Serializable {

	float[] coords;

    /**
     * Initializes x and y by getting ways' first node and getting their lat and long.
     * @param rel Relation from parser
     */
	public MultiPolyline(Relation rel) {
		for (Way way : rel) add(new Polyline(way));
		float x=rel.get(0).getFirst().getLon();
		float y=rel.get(0).getFirst().getLat();
		coords=new float[]{x,y};

	}

	@Override
	public void stroke(GraphicsContext gc) {
		gc.beginPath();
		trace(gc);
		gc.stroke();
	}

	public void trace(GraphicsContext gc) {
		for (Polyline p : this) p.trace(gc);
	}

	@Override
	public void fill(GraphicsContext gc) {
		gc.beginPath();
		trace(gc);
		gc.fill();
	}

    /**
     * Needs to be here due to implementing Drawable interface. Therefore, it has no body.
     * @param gc parsing GraphicsContext
     * @param zoomFactor parsing zoomFactor
     * @param image parsing Image
     */
	public void draw(GraphicsContext gc, double zoomFactor, Image image)
	{

	}

	public float[] getCoord(){

		return coords;
	}


	@Override
	public float getMaxx() {
		return coords[coords.length-2];
	}

	@Override
	public float getMaxy() {
		return coords[coords.length-1];
	}

	@Override
	public float getMinx() {
		return coords[0];
	}

	@Override
	public float getMiny() {
		return coords[1];
	}


	@Override
	public long getNearestIdOfNode(float lon, float lat) {
		return 0;
	}
}
