package Kd;
import Draw.Drawable;
import java.io.Serializable;
import java.util.*;

/**
 * The Kd-tree class uses
 */
public class TreeCustom implements Serializable{

    private Node root;
    private int i;
    private int size;

    List<Drawable> drawables;

    /**
     * Creates the KD Tree and initializes the root as null and drawables as an empty ArrayList
     */
    public TreeCustom(){
        root=null;
        drawables=new ArrayList<>();

    }

    /**
     * Inserts list of drawables into kd-tree.
     * This method starts a recursion that creates the tree.
     * @param insertedDrawables The list of drawables that is going to be made into a tree

     */
    public void insert(List<Drawable> insertedDrawables){
        root = insert(insertedDrawables, true);
        size = insertedDrawables.size();
    }


    /**
     * A recursive method that inserts each drawable into the KD Tree
     * @param insertedDrawables a list of Drawables that gets smaller for each node it travereses down through.
     * @param vertical whether the node is going to split  according to x-coordinates or y-coordinates
     * @return returns a Node recursively.
     */
    private Node insert(List<Drawable> insertedDrawables, final boolean vertical) {

        if (vertical) {
            sortByX(insertedDrawables);
        } else {
            sortByY(insertedDrawables);
        }

        Drawable median = insertedDrawables.get((insertedDrawables.size() / 2));

        List<Drawable> leftList = new ArrayList<>(insertedDrawables.subList(0, (insertedDrawables.size() / 2)));
        List<Drawable> rightList = new ArrayList<>(insertedDrawables.subList((insertedDrawables.size() / 2) + 1, insertedDrawables.size()));

        Node left = null;
        if (leftList.size() > 0) {
            left = insert(leftList, !vertical);
        }

        Node right = null;
        if(rightList.size() > 0) {
            right = insert(rightList, !vertical);
        }

        return new Node(median, left, right, vertical);
    }


    /**
     * Starts a recursion that makes a list of the Drawables that are within a boundary given by arbitrary sets of x and y coordinates.
     * @param minx the minimum x-coordinate of the boundary
     * @param miny the minimum y-coordinate of the boundary
     * @param maxx the maximum x-coordinate of the boundary
     * @param maxy the maximum y-coordinate of the boundary
     */
    public void getList( float minx, float miny, float maxx,float maxy){

        if(root==null) return;
        drawables.clear();
        getList(root, minx, miny, maxx, maxy);
        i=0;

    }

    /**
     * Recursively makes a list of the Drawables that are within a boundary given by arbitrary sets of x and y coordinates.
     * @param node the Node of the KD tree that is being traveresed into accordingly.
     * @param minx the minimum x-coordinate of the boundary
     * @param miny the minimum y-coordinate of the boundary
     * @param maxx the maximum x-coordinate of the boundary
     * @param maxy the maximum y-coordinate of the boundary
     */
    private void getList(Node node, float minx, float miny, float maxx, float maxy){

        addToDrawables(node);

        Node left = node.getLeft();
        Node right = node.getRight();

        boolean isLeftOf;
        boolean isRightOf;
        boolean isInside;

        if (node.isVertical()){
             isLeftOf = node.getMaxx() < minx;
             isRightOf = node.getMinx() > maxx;
        } else {
             isLeftOf = node.getMaxy() < miny;
             isRightOf = node.getMiny() > maxy;
        }

        isInside = !isLeftOf && !isRightOf;
        if (right != null && (isLeftOf || isInside)) {
            getList(right, minx, miny, maxx, maxy);
        }
        if (left != null && (isRightOf || isInside)) {
            getList(left, minx, miny, maxx, maxy);
        }
    }




    /**
     * A helper method that adds alle the Drawables contained in a node to the list of drawables
     * @param node the node containing Drawable elements that will be put into the list of Drawables
     */
    private void addToDrawables(Node node) {

        drawables.addAll(node.getDrawables());
    }


    /**
     * This method returns the list of Drawables that has been added with the getlist method
     * @return a list of Drawables
     */
    public List<Drawable> getDrawables(){
        return drawables;
    }

    /**
     *Sorts a list of Drawables according to the center x-coordinates
     * @param insertedDrawables list to be sorted
     * @return sorted list
     */
    private List<Drawable> sortByX(List<Drawable> insertedDrawables){
        Collections.sort(insertedDrawables, new Comparator<Drawable>() {
                public int compare(Drawable o1, Drawable o2) {
                    float centerx1 = o1.getMinx() + (o1.getMaxx() - o1.getMinx()) / 2;
                    float centerx2 = o2.getMinx() + (o2.getMaxx() - o2.getMinx()) / 2;
                    if(centerx1 > centerx2){
                        return 1;
                    }
                    else if(centerx1 < centerx2){
                        return -1;
                    }
                    else return 0;
                }
            }
        );
        return insertedDrawables;
    }

    /**
     *Sorts a list of Drawables according to the center y-coordinates
     * @param insertedDrawables list to be sorted
     * @return sorted list
     */
    private List<Drawable> sortByY(List<Drawable> insertedDrawables){
        Collections.sort(insertedDrawables, new Comparator<Drawable>() {
                    public int compare(Drawable o1, Drawable o2) {
                        float centery1 = o1.getMiny() + (o1.getMaxy() - o1.getMiny()) / 2;
                        float centery2 = o2.getMiny() + (o2.getMaxy() - o2.getMiny()) / 2;
                        if(centery1 > centery2){
                            return 1;
                        }
                        else if(centery1 < centery2){
                            return -1;
                        }
                        else return 0;
                    }
                }
        );
        return insertedDrawables;
    }


    /**
     * Starts a recursion through the KD tree to find the neares neighbor of a point given by an x and y coordinate.
     * @param queryX the x-coordinate of the query point
     * @param queryY the y-coordinate of the query point
     * @return returns the kd node with the nearest coordinates
     */
    public Node nearest(float queryX, float queryY){
        if(root == null) {
            return null;
        }
        Node result = nearest(root, queryX, queryY, root);
        return result;
    }

    /**
     * recursively goes through the kd-tree to find the nearest neighbor of a point given by an x and y coordinate.
     * @param node the node currently being traversed through.
     * @param queryX the x-coordinate of the query point
     * @param queryY the y-coordinate of the query point
     * @param currentNearest the Node that is currently the nearest to the query point
     * @return the Node that is closest to the query point.
     */
    private Node nearest(Node node, float queryX, float queryY, Node currentNearest){
        float currentNearestDist = getNearestDistanceOfNode(currentNearest, queryX, queryY);

        if(node==null){return currentNearest;}

        float distance = getNearestDistanceOfNode(node, queryX, queryY);

        if(distance <= currentNearestDist){
            currentNearest=node;
        }

        float axisLength;
        if(node.isVertical()){
            axisLength=queryX-node.getMinx();
        }
        else {
            axisLength=queryY-node.getMiny();
        }

        Node first, second;
        if(axisLength<0){
            first=node.getLeft();
            second=node.getRight();
        }
        else{
            first=node.getRight();
            second=node.getLeft();
        }

        currentNearest = nearest(first, queryX, queryY, currentNearest);
        currentNearest = nearest(second, queryX, queryY, currentNearest);

        currentNearestDist = getNearestDistanceOfNode(currentNearest, queryX, queryY);
        currentNearest.setNearDist(currentNearestDist);

        return currentNearest;

    }


    /**
     * a helper method to the nearest method. Goes through all the coordinates of the node's representative Drawable
     * to get the distance of the point that is closest to the query point
     * @param node the node currently being traversed through.
     * @param queryX the x-coordinate of the query point
     * @param queryY the y-coordinate of the query point
     * @return returns the nearest distance found
     */
    private float getNearestDistanceOfNode(Node node, float queryX, float queryY) {

        float nearestOf=100;

        for(i=0;i<node.getRepresentative().getCoord().length;i=i+2){

            float possibleNearest=distanceBetween(node.getRepresentative().getCoord()[i],node.getRepresentative().getCoord()[i+1],queryX,queryY);

            if(possibleNearest<nearestOf){
                nearestOf=possibleNearest;
            }
        }
        return nearestOf;
    }



    /**
     * returns the distance between two given points
     * @param fromx the x-coordinate of the point to measure the distance from
     * @param fromy the y-coordinate of the point to measure the distance from
     * @param tox the x-coordinate of the point to measure the distance to
     * @param toy the y-coordinate of the point to measure the distance to
     * @return distance between the two points
     */
    public float distanceBetween(float fromx, float fromy, float tox, float toy){
        return (float) Math.sqrt(((fromx-tox)*(fromx-tox))+((fromy-toy)*(fromy-toy)));
    }


    public int getSize() {
        return size;
    }

}


