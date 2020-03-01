package TestClasses;

import All.Address;
import Draw.Drawable;
import Draw.WayType;
import Kd.TreeCustom;

import OSM.*;
import Path.EdgeWeightedDigraph;

import OSM.Parser;
import OSM.Polyline;
import OSM.Reader;
import OSM.iModel;
import Path.iDirectedEdge;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
/**
 * StubModel for testing purposes
 */

public class StubModel implements iModel {

    Reader reader;
    Parser parser;
    ArrayList<Address> addressList;
    String[] streetList;
    HashMap<String, ArrayList<String>> stringNoHouseAddressMap;
    HashMap<String, Address> stringAddressMap;
    private HashMap<Polyline, String> roadnameMap;
    private Iterable<iDirectedEdge> edgeList;

    float lonfactor = 1.0f;
    Map<WayType,List<Drawable>> ways = new EnumMap<>(WayType.class);{
        for (WayType type : WayType.values()) {
            ways.put(type, new ArrayList<>());
        }
    }



    float minlat, minlon, maxlat, maxlon;


    public StubModel(String filename, InputStream input) throws IOException, XMLStreamException, ClassNotFoundException {

        preload(filename, input);
    }

    public StubModel(){
        this.edgeList = new Stack<>();
    }


    public void preload(String filename, InputStream input) throws XMLStreamException, IOException, ClassNotFoundException {
        Parser parser=new Parser(this);

        reader=new Reader(filename, input, parser,this);
        this.parser = parser;
        parser.makeTrees();
        System.out.println("yo");
    }

    public Iterable<Drawable> getWaysOfType (WayType type){
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

    public Parser getParser() {
        return parser;
    }


    @Override
    public Map<WayType, TreeCustom> getTrees() {
        return null;
    }


    public void setTrees(Map<WayType, TreeCustom> trees){}

    public ArrayList<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(ArrayList<Address> addressList) {
        this.addressList = addressList;
    }


    public void makeStringAddressMap() {
        HashMap<String, Address> addressStringMap = new HashMap<>();
        for (Address a: getAddressList()){
            addressStringMap.put(a.toString(), a);
        }
        this.stringAddressMap = addressStringMap;
    }

    public HashMap<String, Address> getStringAddressMap(){return stringAddressMap;}

    public void makeStringNoHouseAddressMap() {
        HashMap<String, ArrayList<String>> stringNoHouseAddressMap = new HashMap<>();
        for (Address a: getAddressList()){
            stringNoHouseAddressMap.put(a.toStringNoHouse(), new ArrayList<>());
        }
        this.stringNoHouseAddressMap =stringNoHouseAddressMap;
    }

    public HashMap<String, ArrayList<String>> getStringNoHouseAddressMap(){return stringNoHouseAddressMap;}

    @Override
    public void setPinFrom(Drawable pinFrom) {

    }

    @Override
    public Drawable getPinFrom() {
        return null;
    }

    @Override
    public void setPinTo(Drawable pinTo) {

    }

    @Override
    public Drawable getPinTo() {
        return null;
    }

    public ArrayList<Node> getPathNodes(){
        return null;
    }
    public void setPathNodes(ArrayList<Node> pathNodes){

    }

    @Override
    public void setEdgeWeightedDigraph(EdgeWeightedDigraph graph) {
    }

    @Override
    public EdgeWeightedDigraph getEdgeWeightedDigraph() {
        return null;
    }

    @Override
    public void setRoadWayList(ArrayList<Way> roadWayList) {

    }
    @Override
    public ArrayList<Way> getRoadWayList() {
        return null;
    }

    @Override
    public void setDrawRoute(boolean drawRoute) {

    }

    @Override
    public boolean getDrawRoute() {
        return false;
    }



    public void setRoadnamesMap(HashMap<Polyline, String> roadnameMap){
        this.roadnameMap = roadnameMap;
    }


    @Override
    public void initEdgeWeightedDigraph() {

    }


    @Override
    public void setPathEdges(Iterable<iDirectedEdge> edgeList) {
        this.edgeList = edgeList;

    }

    @Override
    public Iterable<iDirectedEdge> getPathEdges() {
        return edgeList;
    }

}

