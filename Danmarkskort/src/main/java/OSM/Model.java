package OSM;

import All.*;
import Draw.Drawable;
import Draw.WayType;
import Kd.TreeCustom;
import Path.EdgeWeightedDigraph;
import Path.Initializer;
import Path.iDirectedEdge;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.*;

/**
 * This class contains nearly all of the important data of the program. It is responsible for making the parser and the reader plus
 * storing their resulted data.
 */
public class Model implements iModel, Serializable {
    private Reader reader;
    private TreeCustom kdTree;
    private List<Long> ids;
    private Parser parser;
    private float lonfactor = 1.0f;
    private String newfile;
    private ArrayList<Address> addressList;
    private HashMap<String, Address> stringAddressMap;
    private Map<WayType,List<Drawable>> ways = new EnumMap<>(WayType.class);
    private ArrayList<Node> pathNodes;
    private Iterable<iDirectedEdge> pathEdges;
    private HashMap<String, ArrayList<String>> stringNoHouseAddressMap;
    private ArrayList<Way> roadWayList;
    private EdgeWeightedDigraph edgeWeightedDigraph;
    private boolean drawRoute = false;
    private Drawable pinFrom;
    private Drawable pinTo;
    private HashMap<Polyline, String> roadnameMap;
    {
        for (WayType type : WayType.values()) {
            ways.put(type, new ArrayList<>());
        }
    }
    private Map<WayType, TreeCustom> trees = new EnumMap<>(WayType.class);
    {
        for (WayType type : WayType.values()) {
            trees.put(type, new TreeCustom());
        }
    }
    private float minlat, minlon, maxlat, maxlon;


    /**
     * Creates model
     *
     * @param filename file to read
     * @throws IOException            Exception when reading file
     * @throws XMLStreamException     Exception from xml file
     * @throws ClassNotFoundException Exception if class could not be found
     */
    public Model(String filename, InputStream input) throws IOException, XMLStreamException, ClassNotFoundException {
        preload(filename, input);
    }

    /**
     * Loads file
     *
     * @param filename File to read
     * @throws IOException            Exception when reading file
     * @throws XMLStreamException     Exception from xml file
     * @throws ClassNotFoundException Exception if class could not be found
     */
    public void preload(String filename, InputStream input) throws XMLStreamException, IOException, ClassNotFoundException {
        Parser parser = new Parser(this);

        reader = new Reader(filename, input, parser, this);
        this.parser = parser;
        parser.makeTrees();
    }

    /**
     *  makes the EdgeWeightedDigraph
     */
    public void initEdgeWeightedDigraph()
    {
        Initializer roadInitializer = new Initializer(roadWayList, this);
        setEdgeWeightedDigraph(roadInitializer.getGraph());
    }


    /**
     * makes a map with strings as keys and Address as value
     */
    public void makeStringAddressMap() {
        HashMap<String, Address> addressStringMap = new HashMap<>();
        for (Address a : getAddressList()) {
            addressStringMap.put(a.toString(), a);
        }
        this.stringAddressMap = addressStringMap;
    }

    /**
     * Takes all adresses and uses toStringNoHouse() and maps adresses without numbers with a list with all numbers.
     */
    public void makeStringNoHouseAddressMap() {
        HashMap<String, ArrayList<String>> stringNoHouseAddressMap = new HashMap<>();
        for (Address a : getAddressList()) {
            String noHouse = a.toStringNoHouse();
            try {
                if (!stringNoHouseAddressMap.get(noHouse).contains(a.toString())) {
                    stringNoHouseAddressMap.get(noHouse).add(a.toString());
                }
            } catch (NullPointerException e) {
                stringNoHouseAddressMap.put(noHouse, new ArrayList<>());
                stringNoHouseAddressMap.get(noHouse).add(a.toString());
            }
        }
        this.stringNoHouseAddressMap = stringNoHouseAddressMap;
    }

    /**
     *It finds all the nearest kd.Node of all HIGHWAY waytypes and returns them.
     * @param x coordinate
     * @param y coordinate
     * @param transportType parses vehicle, walking or bicycle
     * @return kd.Node (nearest)
     */
    public Kd.Node nearest(float x, float y, String transportType) {

        Kd.Node nearest = null;
        float nearestDist = 100;


        if (transportType.equals("vehicle")) {
            for (WayType wayType : trees.keySet()) {
                if (wayType.toString().startsWith("HIGHWAY") && trees.get(wayType).getSize() != 0) {
                    if (wayType.toString() == "HIGHWAY_CYCLEWAY" || wayType.toString() == "HIGHWAY_FOOTWAY"
                            || wayType.toString() == "HIGHWAY_TRACK" || wayType.toString() == "HIGHWAY_STEPS"
                            || wayType.toString() == "HIGHWAY_PATH") {
                        continue;
                    }
                    Kd.Node possibleNearest = trees.get(wayType).nearest(x, y);

                    if (possibleNearest.getNearDist() < nearestDist) {
                        nearest = possibleNearest;
                        nearestDist = nearest.getNearDist();
                    }
                }
            }
        } else if (transportType.equals("walking")) {
            for (WayType wayType : trees.keySet()) {
                if (wayType.toString().startsWith("HIGHWAY") && trees.get(wayType).getSize() != 0) {
                    if (wayType.toString() == "HIGHWAY_MOTORWAY" || wayType.toString() == "HIGHWAY_CYCLEWAY" || wayType.toString() == "HIGHWAY_MOTORWAY_LINK") {
                        continue;
                    }
                    Kd.Node possibleNearest = trees.get(wayType).nearest(x, y);

                    if (possibleNearest.getNearDist() < nearestDist) {
                        nearest = possibleNearest;
                        nearestDist = nearest.getNearDist();
                    }
                }
            }
        } else if(transportType.equals("bicycle")){
            for (WayType wayType : trees.keySet()) {
                if (wayType.toString().startsWith("HIGHWAY") && trees.get(wayType).getSize() != 0) {
                    if (wayType.toString() == "HIGHWAY_MOTORWAY" || wayType.toString() == "HIGHWAY_FOOTWAY" || wayType.toString() == "HIGHWAY_MOTORWAY_LINK"
                            || wayType.toString() == "HIGHWAY_STEPS") {
                        continue;
                    }
                    Kd.Node possibleNearest = trees.get(wayType).nearest(x, y);

                    if (possibleNearest.getNearDist() < nearestDist) {
                        nearest = possibleNearest;
                        nearestDist = nearest.getNearDist();
                    }
                }
            }
        }
        else
        {
            for (WayType wayType : trees.keySet()) {
                if (wayType.toString().startsWith("HIGHWAY") && trees.get(wayType).getSize() != 0) {
                    Kd.Node possibleNearest = trees.get(wayType).nearest(x, y);


                    if (possibleNearest.getNearDist() < nearestDist) {
                        nearest = possibleNearest;
                        nearestDist = nearest.getNearDist();
                    }
                }
            }
        }
        return nearest;
    }


    public HashMap<String, Address> getStringAddressMap() {
        return stringAddressMap;
    }

    public ArrayList<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(ArrayList<Address> addressList) {
        this.addressList = addressList;
    }

    public HashMap<String, ArrayList<String>> getStringNoHouseAddressMap() {
        return stringNoHouseAddressMap;
    }

    public void setPinFrom(Drawable pinFrom) {
        this.pinFrom = pinFrom;
    }

    public Drawable getPinFrom() {
        return pinFrom;
    }

    public void setPinTo(Drawable pinTo) {
        this.pinTo = pinTo;
    }

    public Drawable getPinTo() {
        return pinTo;
    }


    public TreeCustom getKdTree() {
        return kdTree;
    }


    public Iterable<Drawable> getWaysOfType(WayType type) {
        return ways.get(type);
    }

    public float getMinlat() {
        return minlat;
    }

    public float getMinlon() {
        return minlon;
    }

    public float getMaxlat() {
        return maxlat;
    }

    public float getMaxlon() {
        return maxlon;
    }

    public void setMinlat(float minlat) {
        this.minlat = minlat;
    }

    public void setMinlon(float minlon) {
        this.minlon = minlon;
    }

    public void setMaxlat(float maxlat) {
        this.maxlat = maxlat;
    }

    public void setMaxlon(float maxlon) {
        this.maxlon = maxlon;
    }

    public float getLonfactor() {
        return lonfactor;
    }

    public void setLonfactor(float lonfactor) {
        this.lonfactor = lonfactor;
    }

    public void setWays(Map<WayType, List<Drawable>> ways) {
        this.ways = ways;
    }

    public Map<WayType, List<Drawable>> getWays() {
        return ways;
    }

    public Map<WayType, TreeCustom> getTrees() {
        return trees;
    }

    public void setTrees(Map<WayType, TreeCustom> trees) {
        this.trees = trees;
    }

    public void putDrawable(WayType wayType, Drawable drawable) {
        getWays().get(wayType).add(drawable);
    }

    public void removeDrawable(WayType wayType) {
        getWays().get(wayType).removeAll(getWays().get(wayType));
    }

    public ArrayList<Node> getPathNodes(){
        return pathNodes;
    }

    public void setPathNodes(ArrayList<Node> pathNodes) {
        this.pathNodes = pathNodes;
    }

    public void setEdgeWeightedDigraph(EdgeWeightedDigraph graph) {
        this.edgeWeightedDigraph = graph;
    }

    public EdgeWeightedDigraph getEdgeWeightedDigraph() {
        return edgeWeightedDigraph;
    }

    public Iterable<iDirectedEdge> getPathEdges() {
        return pathEdges;
    }

    public void setRoadWayList(ArrayList<Way> roadWayList) {
        this.roadWayList = roadWayList;
    }

    public ArrayList<Way> getRoadWayList() {
        return roadWayList;

    }
    public void setDrawRoute(boolean drawRoute) {
        this.drawRoute = drawRoute;
    }

    public boolean getDrawRoute() {
        return drawRoute;
    }

    public void setPathEdges(Iterable<iDirectedEdge> pathEdges) {
        this.pathEdges = pathEdges;
    }

    public void setRoadnamesMap (HashMap<Polyline, String> roadnameMap){
        this.roadnameMap = roadnameMap;
    }


}

