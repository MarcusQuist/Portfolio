package Kd;
import Draw.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * The KD Node class creates nodes which mainly are
 * used in the KD Tree and route guidance.
 */
public class Node {

    private Node left;
    private Node right;
    private boolean vertical;
    private Drawable drawable;
    private float minx;
    private float miny;
    private float maxx;
    private float maxy;
    private Drawable representative;
    private float nearDist;


    /**
     * Creates node
     * @param drawable list containing all the elements that belong to this node or nodes below
     * @param left Node to the left
     * @param right Node to the right
     * @param vertical wether the node splits by x-axis or y-axisof
     */
    Node(Drawable drawable, Node left, Node right, boolean vertical){
        this.left=left;
        this.right=right;
        this.vertical=vertical;
        this.drawable=drawable;
        this.minx=drawable.getMinx();
        this.miny=drawable.getMiny();
        this.maxx=drawable.getMaxx();
        this.maxy=drawable.getMaxy();
        this.representative=drawable;
    }



    public List<Drawable> getDrawables(){
        ArrayList<Drawable> drawables = new ArrayList();
        drawables.add(drawable);
        return drawables;

    }

    public float getMinx() {
        return minx;
    }

    public float getMiny() {
        return miny;
    }

    public float getMaxx() {
        return maxx;
    }

    public float getMaxy() {
        return maxy;
    }

    public boolean isVertical() {
        return vertical;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public Drawable getRepresentative() {
        return representative;
    }

    public void setNearDist(float nearDist) {
        this.nearDist = nearDist;
    }

    public float getNearDist() {
        return nearDist;
    }
}
