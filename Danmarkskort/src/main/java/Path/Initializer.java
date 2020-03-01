package Path;

import OSM.Node;
import OSM.Way;
import OSM.iModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *  The {@code Initializer} class initializes the edgeWeightedDigraph used
 *  for the route guidance.
 *
 *  The constructurs take an ArrayList of all ways to be added to the digraph,
 *  and the model.
 *
 *
 *  All unique nodes in each way of the passed ArrayList is then added to a HashMap,
 *  which allows to get a node from a local ID.
 *  Afterwards, each node is added to an Array, which allows to get a local ID from a node.
 *
 *  The Digraph is then initialized with the paramameters: the idToNode size, the HashMap nodeToInt,
 *  the Array idToNode, and the model.
 *
 */

public class Initializer
{
    private ArrayList<Way> wayList;
    private iModel model;
    public Initializer(ArrayList<Way> wayList, iModel model)
    {
        this.model = model;
        this.wayList = wayList;
        initNodeToIdMap(wayList);
        initIdToNode();
        initGraph(model);

    }

    private Node[] idToNode;
    private int index = 0;

    /**
     * Places each unique node inside of a HashMap.
     * The HashMap's key is a node, and the valeu is a local ID.
     *
     * @param wayList is the ArrayList of all Ways allowed to create
     *                a route from
     */
    private HashMap<Node, Integer> nodeToIdMap;
    private void initNodeToIdMap(ArrayList<Way> wayList)
    {
        nodeToIdMap = new HashMap<>();
        for(Way way : wayList)
        {
            for (Node node : way)
            {
                if(!nodeToIdMap.containsKey(node))
                {
                    nodeToIdMap.put(node, index);
                    index++;
                }
            }
        }
    }

    /**
     * Places each unique node inside of an Array
     * Each node has a local ID
     */
    private void initIdToNode()
    {
        idToNode = new Node[nodeToIdMap.size()];
        for(Map.Entry<Node, Integer> node : nodeToIdMap.entrySet())
        {
            idToNode[node.getValue()] = node.getKey();
        }
    }

    /**
     * Initializes the EdgeWeightedDigraph used for route guidance, and
     * adds all the possible edges, taken from the nodes of the ways in
     * the ArrayList of total ways.
     *
     * @param model instance of the model
     */
    private EdgeWeightedDigraph graph;
    public void initGraph(iModel model)
    {
        graph = new EdgeWeightedDigraph(idToNode.length, idToNode, nodeToIdMap, model);

        for(Way way : wayList)
        {
            int maxSpeed = way.getMaxSpeed();
            boolean isOneWay = way.getOneWay();
            for(int i = 0; i < way.size()-1; i++)
            {
                Node nodeFrom = way.get(i);
                int v = nodeToIdMap.get(nodeFrom);
                Node nodeTo = way.get(i+1);
                int w = nodeToIdMap.get(nodeTo);
                addEdge(v,w, nodeFrom, nodeTo, maxSpeed, way);
                if(!isOneWay)
                {
                    addEdge(w,v, nodeFrom, nodeTo, maxSpeed, way);
                }
            }
        }
    }

    /**
     * Adds an edge to the EdgeWeightedDigraph.
     * @param v the local id of the from node
     * @param w the local id of the to node
     * @param nodeFrom the node object of the from node
     * @param nodeTo the node object of the to node
     * @param maxSpeed the maximum speed allowed on the given edge
     * @param way the given way which the nodes of the edge is a part of
     */

    public void addEdge(int v, int w, Node nodeFrom, Node nodeTo, int maxSpeed, Way way)
    {
        String street;

        street = way.getStreet();
        DirectedEdge e = new DirectedEdge(v, w, nodeFrom, nodeTo, maxSpeed, model.getLonfactor(), way.getVehicleAllowed(), way.getBicycleAllowed(), way.getWalkingAllowed(), street,way.getOneWay());

        graph.addEdge(e);
    }

    /**
     * Returns the EdgeWeightedDigraph
     * @return the EdgeWeightedDigraph
     */
    public EdgeWeightedDigraph getGraph()
    {
        return graph;
    }
}
