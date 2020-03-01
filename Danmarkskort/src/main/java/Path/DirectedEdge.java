package Path;

import OSM.Node;
import OSM.iModel;
/**
 *  The {@code DirectedEdge} class represents a weighted edge in an
 *  {@link EdgeWeightedDigraph}. Each edge consists of two integers
 *  (ID's of the two vertices), two nodes representing the two vertices,
 *  the maximum speed allowed, 3 booleans specifying the allowed transport types,
 *  the length of the edge in a real-value, and a street name.
 *
 *  The data type
 *  provides methods for accessing the two endpoints of the directed edge and
 *  the weight.
 */

public class DirectedEdge implements iDirectedEdge{
    private final int v;
    private final int w;
    private int maxSpeed;

    private boolean vehicleAllowed;
    private boolean bicycleAllowed;
    private boolean walkingAllowed;
    private boolean oneWay;

    private final double weight;
    private String street;

    /**
     * Initializes a directed edge from vertex {@code v} to vertex {@code w} and
     * calculates the weight.
     * @param v the tail vertex
     * @param w the head vertex
     * @param nodeFrom the from node
     * @param nodeTo the to node
     * @param maxSpeed the maximum allowed speed
     * @param lonFactor the model
     * @param vehicleAllowed whether vehicles are allowed
     * @param bicycleAllowed whether bicycles are allowed
     * @param walkingAllowed whether walking is allowed
     * @param street the name of the street
     * @oneWay whether the way of the edge is oneway
     *
     * @throws IllegalArgumentException if either {@code v} or {@code w}
     *    is a negative integer
     * @throws IllegalArgumentException if {@code weight} is {@code NaN}
     */
    public DirectedEdge(int v, int w, Node nodeFrom, Node nodeTo, int maxSpeed, float lonFactor, boolean vehicleAllowed, boolean bicycleAllowed, boolean walkingAllowed, String street, boolean oneWay) {
        this.v = v;
        this.w = w;

        this.maxSpeed = maxSpeed;
        this.vehicleAllowed = vehicleAllowed;
        this.bicycleAllowed = bicycleAllowed;
        this.walkingAllowed = walkingAllowed;
        this.oneWay = oneWay;

        this.street = street;

        this.weight = Haversine.distance(nodeFrom.getLat(), nodeFrom.getLon(), nodeTo.getLat(), nodeTo.getLon(), lonFactor);

        if (this.v < 0) throw new IllegalArgumentException("Vertex names must be nonnegative integers");
        if (this.w < 0) throw new IllegalArgumentException("Vertex names must be nonnegative integers");
        if (Double.isNaN(weight)) throw new IllegalArgumentException("Weight is NaN");
    }


    /**
     * Returns the tail vertex of the directed edge.
     * @return the tail vertex of the directed edge
     */
    public int from() {
        return v;
    }

    /**
     * Returns the head vertex of the directed edge.
     * @return the head vertex of the directed edge
     */
    public int to() {
        return w;
    }

    /**
     * Returns the weight of the directed edge.
     * @return the weight of the directed edge
     */
    public double weight() {
        return this.weight;
    }

    /**
     * Returns the maximum speed allowed
     * @return the maximum sped of the edge
     */
    public int getMaxSpeed()
    {
        return maxSpeed;
    }

    /**
     * Returns if vehicles are allowed on the edge
     * @return if vehicles are allowed on the edge
     */
    public boolean getVehicleAllowed()
    {
        return vehicleAllowed;
    }

    /**
     * Returns if bicycles are allowed on the edge
     * @return if bicycles are allowed on the edge
     */
    public boolean getBicycleAllowed()
    {
        return bicycleAllowed;
    }

    /**
     * Returns if walking is allowed on the edge
     * @return if walking is allowed on the edge
     */
    public boolean getWalkingAllowed()
    {
        return walkingAllowed;
    }

    /**
     * Returns if the way of the edge is one way
     * @return if the way of the edge is one way
     */
    public boolean oneWay() {
        return oneWay;
    }

    /**
     * Returns a string representation of the directed edge.
     * @return a string representation of the directed edge
     */
    public String toString() {
        return v + "->" + w + " " + String.format("%5.2222222222f", weight);
    }

    /**
     * Returns the vertice v
     * @return the vertice v
     */
    public int getV() {
        return v;
    }

    /**
     * Returns the vertice w
     * @return the vertice w
     */
    public int getW() {
        return w;
    }

    /**
     * Returns the street of the edge
     * @return the street of the edge
     */
    public String getStreet()
    {
        return street;
    }
}



