package OSM;

import Draw.Drawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.Serializable;

public class Polyline implements Drawable, Serializable{
	private float[] coord;
	float maxx;
	float maxy;
	float minx;
	float miny;
	long ids[];

	public Polyline(Way way) {
		coord = new float[way.size() * 2];
		ids = new long[way.size()];
		for (int i = 0 ; i < way.size() ; i++) {
			ids[i] = way.get(i).getAsLong();
			coord[2*i] = way.get(i).getLon();
			coord[2*i+1] = way.get(i).getLat();
		}

		makeMaxAndMin(coord);
	}


	public void stroke(GraphicsContext gc) {
		gc.beginPath();
		trace(gc);
		gc.stroke();
	}

	public void trace(GraphicsContext gc) {
		gc.moveTo(coord[0], coord[1]);
		for (int i = 2 ; i < coord.length ; i+=2) {
			gc.lineTo(coord[i], coord[i+1]);
		}
	}
	public void draw(GraphicsContext gc, double zoomFactor, Image image)
	{

	}

	public void fill(GraphicsContext gc) {
		gc.beginPath();
		trace(gc);
		gc.fill();
	}

	public float[] getCoord(){
		return coord;
	}

	public float getMaxx() {
		return maxx;
	}

	public float getMaxy() {
		return maxy;
	}

	public float getMinx() {
		return minx;
	}

	public float getMiny() {
		return miny;
	}


	@Override
	public long getNearestIdOfNode(float lon, float lat) {
		double currentBestDistance = Double.POSITIVE_INFINITY;
		long bestId = 0;
		for (int i = 0 ; i < coord.length ; i += 2) {
			float vertlon = coord[i];
			float vertlat = coord[i+1];

			double distance = Math.sqrt(Math.pow(vertlon - lon, 2) + Math.pow(vertlat - lat,2));
			if (distance < currentBestDistance) {
				currentBestDistance = distance;
				bestId = ids[i/2];
			}
		}
		return bestId;
	}

	public void makeMaxAndMin(float[] coords){

		minx=coords[0];
		maxx=coords[0];
		miny=coords[1];
		maxy=coords[1];

		for(int i=0;i<coords.length;i=i+2){
			if(coords[i]<minx){
				minx=coords[i];
			}else if(coords[i]>maxx){maxx=coords[i];}
			if(coords[i+1]<miny){
				miny=coords[i+1];
			}else if(coords[i+1]>maxy){maxy=coords[i+1];}

		}
	}
}
