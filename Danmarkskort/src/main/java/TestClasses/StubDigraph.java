package TestClasses;

import OSM.Node;
import Path.iDigraph;
import Path.iDirectedEdge;
import edu.princeton.cs.algs4.Bag;

import java.util.ArrayList;
/**
 * StubDigraph for testing purposes
 */

public class StubDigraph implements iDigraph {

    ArrayList<iDirectedEdge> edges;

    public StubDigraph() {
        edges=new ArrayList<>();
    }

    @Override
    public Iterable<iDirectedEdge> adj(int i) {
        return null;
    }

    @Override
    public Iterable<iDirectedEdge> edges() {
        return edges;
    }

    @Override
    public Node[] getIdToNode() {
        return new Node[0];
    }

    @Override
    public Bag<iDirectedEdge>[] getAdj() {
        return new Bag[0];
    }

    @Override
    public double getTotalTravelDistance() {
        return 0;
    }

    @Override
    public double getTotalTravelTime() {
        return 0;
    }

    @Override
    public int V() {
        return 5;
    }




}
