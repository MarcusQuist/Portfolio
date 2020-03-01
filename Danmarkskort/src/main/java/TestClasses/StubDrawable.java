package TestClasses;

import Draw.Drawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
/**
 * StubDrawable for testing purposes
 */

public class StubDrawable implements Drawable, Cloneable{

    private float[] coords;

    float maxx;
    float maxy;
    float minx;
    float miny;


    public StubDrawable(float x1, float y1, float x2, float y2, int id){
        coords=new float[4];
        coords[0]=x1;
        coords[1]=y1;
        coords[2]=x2;
        coords[3]=y2;

        makeMaxAndMin(coords);

    }

    public StubDrawable(float x1, float y1, float x2, float y2, float x3, float y3, int id){
        coords=new float[6];
        coords[0]=x1;
        coords[1]=y1;
        coords[2]=x2;
        coords[3]=y2;
        coords[4]=x3;
        coords[5]=y3;

        makeMaxAndMin(coords);

    }

    @Override
    public void stroke(GraphicsContext gc) {

    }

    @Override
    public void fill(GraphicsContext gc) {

    }
    public void draw(GraphicsContext gc, double zoomFactor, Image image)
    {

    }

    @Override
    public float[] getCoord() {
        return coords;
    }


    @Override
    public float getMinx() {
        return minx;
    }

    @Override
    public float getMiny() {
        return miny;
    }



    @Override
    public long getNearestIdOfNode(float lon, float lat) {
        return 0;
    }

    @Override
    public float getMaxx() {
        return maxx;
    }

    @Override
    public float getMaxy() {
        return maxy;
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
