package OSM;

import All.Address;
import Draw.Drawable;
import Draw.WayType;
import Kd.TreeCustom;
import Path.DirectedEdge;
import Path.EdgeWeightedDigraph;
import Path.iDirectedEdge;
import edu.princeton.cs.algs4.Stack;

import java.util.*;

/**
 * Interface of Model
 */
public interface iModel {


    Map<WayType, List<Drawable>> ways = new EnumMap<>(WayType.class);
    Iterable<Drawable> getWaysOfType (WayType type);


    float getMinlat();

    float getMinlon();

    float getMaxlat();

    float getMaxlon();

    Map<WayType, TreeCustom> getTrees();

    Map<WayType, List<Drawable>> getWays();

    float getLonfactor();


    void setMinlat(float minlat);

    void setMinlon(float minlon);

    void setMaxlat(float maxlat);

    void setMaxlon(float maxlon);

    void setTrees(Map<WayType, TreeCustom> trees);

    void setWays(Map<WayType, List<Drawable>> ways);

    void setLonfactor(float lonfactor);

    ArrayList<Address> getAddressList();
    void setAddressList(ArrayList<Address> addressList);
    void makeStringAddressMap();
    HashMap<String, Address> getStringAddressMap();
    void makeStringNoHouseAddressMap();
    HashMap<String, ArrayList<String>> getStringNoHouseAddressMap();

    void setPinFrom(Drawable pinFrom);
    Drawable getPinFrom();
    void setPinTo(Drawable pinTo);
    Drawable getPinTo();

    ArrayList<Node> getPathNodes();
    void setPathNodes(ArrayList<Node> pathNodes);

    void setEdgeWeightedDigraph(EdgeWeightedDigraph graph);
    EdgeWeightedDigraph getEdgeWeightedDigraph();

    void setRoadWayList(ArrayList<Way> roadWayList);
    ArrayList<Way> getRoadWayList();

    void setDrawRoute(boolean drawRoute);
    boolean getDrawRoute();


    void setPathEdges(Iterable<iDirectedEdge> edgeList);

    Iterable<iDirectedEdge> getPathEdges();
    void setRoadnamesMap(HashMap<Polyline, String> roadnameMap);

    void initEdgeWeightedDigraph();

}
