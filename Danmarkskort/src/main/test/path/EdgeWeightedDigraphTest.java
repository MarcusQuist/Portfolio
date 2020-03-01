package path;

import OSM.Node;
import OSM.Way;
import Path.*;
import TestClasses.StubDirectedEdge;
import TestClasses.StubModel;
import edu.princeton.cs.algs4.Stack;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import Path.EdgeWeightedDigraph;
import Path.NoPathException;
import Path.Initializer;
import Path.iDirectedEdge;
import Path.DijkstraSP;
import Path.DirectedEdge;

public class EdgeWeightedDigraphTest
{
    private Node node0;
    private Node node1;
    private Node node2;
    private Node node3;
    private Node node4;
    private Node node5;
    private Node node6;
    private Node node7;
    private EdgeWeightedDigraph digraph;
    private ArrayList<Way> wayList;

    private StubModel model;

    @Before
    public void initializeGraph(){
        node0 = new Node(0, 1,1);
        node1 = new Node(1, 1,2);
        node2 = new Node(2, 2,3);
        node3 = new Node(3, 2,4);
        node4 = new Node(4, 3,1);
        node5 = new Node(5, 3,2);
        node6 = new Node(6, 3,1);
        node7 = new Node(7, 3,2);

        wayList = new ArrayList<>();

        Way way1 = new Way(1);
        way1.add(node0);
        way1.add(node1);
        way1.isOneWay();
        way1.setVehicleAllowed();
        wayList.add(way1);

        Way way2 = new Way(2);
        way2.add(node2);
        way2.add(node3);
        way2.setVehicleAllowed();
        way2.setBicycleAllowed();
        way2.setWalkingAllowed();
        wayList.add(way2);

        Way way3 = new Way(3);
        way3.add(node3);
        way3.add(node4);
        way3.add(node5);
        way3.setVehicleAllowed();
        wayList.add(way3);

        Way way4 = new Way(4);
        way4.add(node3);
        way4.add(node6);
        way4.add(node7);
        way4.add(node4);
        way4.setVehicleAllowed();
        way4.setWalkingAllowed();
        wayList.add(way4);

        model = new StubModel();
        Initializer initializer = new Initializer(wayList, model);
        digraph = initializer.getGraph();
    }
    @Test
    public void vTest()
    {
        assertEquals(8, digraph.V());
    }

    @Test
    public void eTest()
    {
        assertEquals(13, digraph.E());
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateVertexTest()
    {
        StubDirectedEdge edge1 = new StubDirectedEdge(-10,2,12);
        digraph.addEdge(edge1);
    }

    @Test
    public void addEdgeTest()
    {
        StubDirectedEdge edge1 = new StubDirectedEdge(1,2,10);
        digraph.addEdge(edge1);
        assertEquals(14, digraph.E());
    }

    @Test
    public void adjTest()
    {
        Iterable<iDirectedEdge> adj = digraph.adj(0);

        for(iDirectedEdge e : adj)
        {
                // Test from node is correct
                assertEquals(0, e.getV());
                // Test to node is correct
                assertEquals(1, e.getW());
        }
    }
    @Test
    public void edgesTest()
    {
        Iterable<iDirectedEdge> list = digraph.edges();
        int v = 0;
        for(iDirectedEdge e : list)
        {
            if(e.from() == 1)
            {
                v = e.from();
            }
        }
        assertNotSame(1, v);
        for(iDirectedEdge e : list)
        {
            if(e.from() == 2)
            {
                v = e.from();
            }
        }
        assertEquals(2, v);
    }

    @Test(expected = NoPathException.class)
    public void pathFromToExceptionTest()
    {
        digraph.pathFromTo(node1, node2, "vehicle");
    }

    @Test(expected = Test.None.class)
    public void pathFromToTest()
    {
        digraph.pathFromTo(node2, node5, "vehicle");
    }

    @Test(expected = AssertionError.class)
    public void pathFromToDisallowedTransportTypeTest()
    {
        // Not relaxing nodes if the edge is not of the transport type
        digraph.pathFromTo(node2, node5, "bicycle");
    }

    @Test
    public void initDijkstraCorrectShortestPathChosenTest()
    {
        DijkstraSP sp = new DijkstraSP(digraph, 2, "vehicle");
        Iterable<iDirectedEdge> edgeList = null;
        if (sp.hasPathTo(5, "vehicle"))
        {
            edgeList = sp.pathTo(5, "vehicle");
        }
        Stack<iDirectedEdge> expected = new Stack<iDirectedEdge>();
        for(iDirectedEdge e : edgeList)
        {
            System.out.println("v: " + e.from() + " -> " + e.to());
            expected.push(e);
        }

        Stack<iDirectedEdge> path = new Stack<iDirectedEdge>();
        DirectedEdge e1 = new DirectedEdge(2,3,node2,node3,0,model,true,true,false,"streetname", false);
        DirectedEdge e2 = new DirectedEdge(3,4,node3,node4,0,model,true,true,false,"streetname", false);
        DirectedEdge e3 = new DirectedEdge(4,5,node4,node5,0,model,true,true,false,"streetname", false);
        path.push(e1);
        path.push(e2);
        path.push(e3);

        assertEquals(path.pop().from(), expected.pop().from());
        assertEquals(path.pop().from(), expected.pop().from());
        assertEquals(path.pop().from(), expected.pop().from());
    }
}
