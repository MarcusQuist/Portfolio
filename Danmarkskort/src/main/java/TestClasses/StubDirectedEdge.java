package TestClasses;

import OSM.Node;
import Path.DirectedEdge;
import Path.iDirectedEdge;
/**
 * StubDirectedEdge for testing purposes
 */

public class StubDirectedEdge implements iDirectedEdge {

    private  int v;
    private  int w;
    private Node nodeFrom;
    private Node nodeTo;
    private int maxSpeed;
    private int id;
    private boolean vehicleAllowed;
    private boolean bicycleAllowed;
    private boolean walkingAllowed;
    private boolean oneWay;
    private  double weight;
    private String street;


    public StubDirectedEdge(int v, int w, Node nodeFrom, Node nodeTo, int maxSpeed, boolean vehicleAllowed, boolean bicycleAllowed, boolean walkingAllowed, String street, boolean oneWay){
        this.v=v;
        this.w=w;
        this.nodeFrom=nodeFrom;
        this.nodeTo=nodeTo;
        this.maxSpeed=maxSpeed;
        this.vehicleAllowed=vehicleAllowed;
        this.bicycleAllowed=bicycleAllowed;
        this.walkingAllowed=walkingAllowed;
        this.street=street;
        this.oneWay=oneWay;
    }

    public StubDirectedEdge(int v, int w, int id){

        this.v=v;
        this.w=w;

        this.id=id;

    }

    public StubDirectedEdge(String street, double weight) {
        this.street=street;
        this.weight=weight;
    }

    @Override
    public int from() {
        return v;
    }

    @Override
    public int to() {
        return w;
    }

    @Override
    public double weight() {
        return weight;
    }

    @Override
    public int getMaxSpeed() {
        return 0;
    }

    @Override
    public boolean getVehicleAllowed() {
        return false;
    }

    @Override
    public boolean getBicycleAllowed() {
        return false;
    }

    @Override
    public boolean getWalkingAllowed() {
        return false;
    }

    @Override
    public boolean oneWay() {
        return false;
    }

    @Override
    public int getV() {
        return 0;
    }

    @Override
    public int getW() {
        return 0;
    }

    @Override
    public String getStreet() {
        return street;
    }
}
