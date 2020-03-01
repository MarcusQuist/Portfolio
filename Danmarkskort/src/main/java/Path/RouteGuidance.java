package Path;
import OSM.Node;
import OSM.iModel;
import edu.princeton.cs.algs4.Stack;
import java.util.ArrayList;

/**
 * Creates a guidance for a route
 */
public class RouteGuidance {
    Stack<iDirectedEdge> localEdge;
    private EdgeWeightedDigraph localGraph;
    private double lengthBeforeTurn;
    private DistanceType distancetype;
    private ArrayList<String> routeGuidanceOutput;
    private String finalDestination;
    private String arrival;
    private String movement;
    private iDirectedEdge d;
    private String transportType;
    private String currentRoad;
    private String nextRoad;
    private String route;
    private String turn;
    private double crossProduct;
    private double angle;

    /**
     * Initialises all the fields needed for the RouteGuidance functionality and methods
     * @param model The parsed model from controller
     * @param transportType The parsed transport type to determine if the person is walking or driving
     */
    public RouteGuidance(iModel model, String transportType) {

        localEdge = (Stack<iDirectedEdge>) model.getPathEdges();
        localGraph = model.getEdgeWeightedDigraph();
        this.distancetype = new DistanceType();
        this.routeGuidanceOutput = new ArrayList<>();
        this.transportType = transportType;
        initRoute();
    }

    /**
     * Initialises the route by getting the size of edges for the route, so it can iterate throught it and
     * make the proper calculations in the method (initPointsCoords). Getting the calculated total travel
     * distance and time.
     */
    public void initRoute() {
        try {
            int size = localEdge.size();
            String totalTravel = distancetype.distance(localGraph.getTotalTravelDistance()) + " FROM POSITION 1 TO POSITION 2";
            String totalTravelTime = "ESTIMATED TIME TO ARRIVAL: " + distancetype.time(localGraph.getTotalTravelTime());
            addToRouteList(totalTravel);
            addToRouteList(totalTravelTime);

            for (int i = 0; i < size; i++) {
                initPointsCoords(size, i);
            }
        }catch (NullPointerException e){
            e.getMessage();
        }


    }

    /**
     * Adding the String output from total travel distance, time and the route guidance containing
     * how far to drive on a certain road, the name of the current road, when to turn and the the next road.
     * @param string to be added to the route list.
     */
    public void addToRouteList(String string) {
        routeGuidanceOutput.add(string);
    }

    /**
     * Using algs4 library to pop and push edges inorder to initialize node a,b,c by a call to the
     * EdgeWeightedDigraph to get the tail and head vertex of an edge by giving getIdtoNode() an id,
     * where the array return a node. All points contains a
     * X and Y coordinate generated from node's latitude and lontitude.
     * @param size the size of the edge stack.
     * @param counter the value of i in the forloop.
     */
    public void initPointsCoords(int size, int counter) {
        if (localEdge != null) {
            d = localEdge.pop();
            lengthBeforeTurn += (d.weight());
            currentRoad = d.getStreet();

            if (!(d.oneWay()) && (localGraph.getAdj()[d.getW()].size() > 2) || (d.oneWay()) && (localGraph.getAdj()[d.getW()].size() > 1)) {


                Node node1 = localGraph.getIdToNode()[d.getV()];
                double ax = node1.getLon();
                double ay = node1.getLat();


                Node node2 = localGraph.getIdToNode()[d.getW()];
                double bx = node2.getLon();
                double by = node2.getLat();

                if (counter != size - 1) {
                    d = localEdge.pop();
                    nextRoad = d.getStreet();
                }


                Node node3 = localGraph.getIdToNode()[d.getW()];
                double cx = node3.getLon();
                double cy = node3.getLat();

                localEdge.push(d);
                calculations(ax, ay, bx, by, cx, cy);
                checkTurn(crossProduct, angle);

                if (route != null) {
                    addToRouteList(route);
                    addToRouteList(turn);
                    route = null;
                    turn = null;
                }
                if (finalDestination != null) {
                    addToRouteList(finalDestination);
                    addToRouteList(arrival);
                    finalDestination = null;
                    arrival = null;
                }

            }
        }
    }

    /**
     * Calculates the crossProduct by using the crossProduct formula provided from the source.
     * Calculated the angle using the formula for the angle between two vectors and transforming it from radians to degrees.
     * @param ax node1's x coordinate.
     * @param ay node1's y coordinate.
     * @param bx node2's x coordinate.
     * @param by node2's y coordinate.
     * @param cx node3's x coordinate.
     * @param cy node3's y coordinate.
     */
    public void calculations(double ax, double ay, double bx, double by, double cx, double cy) {
        bx -= ax;
        by -= ay;
        cx -= ax;
        cy -= ay;
        //crossProduct timed with "1000000000" for readability
        crossProduct = (bx * cy - by * cx) * 1000000000;
        double skaleringsprodukt = (bx * cx) + (by * cy);
        double length = Math.sqrt((Math.pow(bx, 2) + Math.pow(by, 2)) * (Math.pow(cx, 2) + Math.pow(cy, 2)));
        double compute = skaleringsprodukt / length;
        angle = Math.toDegrees(Math.acos(compute));
    }

    /**
     * Taking the parameters to check whether the person travelling should turn. Using angle and crossProduct to determine if there is a left or right turn.
     * If there is one element or below in the localEdge stack, we have arrived at the final destination.
     * @param crossProduct parsed on from the initPointsCoords().
     * @param angle parsed on from the initPointsCoords().
     */
    public void checkTurn(double crossProduct, double angle) {

        if (angle >= 7 && localEdge.size() > 1) {

            if (crossProduct < -5.0) {
                route = determineVehicleType() + distancetype.distance(lengthBeforeTurn) + " down " + currentRoad;
                turn = "Turn right onto " + nextRoad;
                lengthBeforeTurn = 0.0;
                movement = null;
            }
            if (crossProduct > 5.0) {
                route = determineVehicleType() + distancetype.distance(lengthBeforeTurn) + " down " + currentRoad;
                turn = "Turn left onto " + nextRoad;
                lengthBeforeTurn = 0.0;
                movement = null;
            }
        }
        if (localEdge.size() <= 1) {
            finalDestination = determineVehicleType() + distancetype.distance(lengthBeforeTurn) + " to " + currentRoad;
            arrival = "Arrival at destination!";
            localEdge = null;


        }

    }

    /**
     * Returning the Arraylist filled with string containing the route guidance.
     * @return Arraylist of Strings
     */
    public ArrayList<String> getRouteGuidanceOutput() {
        return routeGuidanceOutput;
    }

    /**
     * Determines which vehicle type being used and intializes the movement appropiate to vehicle type.
     * @return movement or null
     */
    public String determineVehicleType(){
        if(transportType.equals("vehicle")){
            movement = "Drive ";
            return movement;
        }
        if(transportType.equals("bicycle")){
            movement = "Drive ";
            return movement;
        }
        if(transportType.equals("walking")){
            movement = "Walk ";
            return movement;
        }
        return null;
    }
}
