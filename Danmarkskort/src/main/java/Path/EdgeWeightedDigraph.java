package Path;
import OSM.Node;
import OSM.iModel;
import edu.princeton.cs.algs4.*;

import java.util.ArrayList;
import java.util.HashMap;
/**
 *  The {@code EdgeWeightedDigraph} class represents an edge-weighted
 *  digraph of vertices named 0 through <em>V</em> - 1, where each
 *  directed edge is of type DirectedEdge and has a real-valued weight.
 *
 *  It supports the following four primary operations: add a directed edge
 *  to the digraph, iterate over all of edges incident from a given vertex,
 *  get a path from a start vertice to an end vertice, and to calculate
 *  the shortest path from a start vertice to an end vertice.
 *
 *  It also provides
 *  methods for returning the number of vertices <em>V</em> and the number
 *  of edges <em>E</em>. Parallel edges and self-loops are permitted.
 *  <p>
 *  This implementation uses an adjacency-lists representation, which
 *  is a vertex-indexed array of Bag objects.
 *  <p>
 */
public class EdgeWeightedDigraph implements iDigraph{
    private static final String NEWLINE = System.getProperty("line.separator");

    private final int V;                // number of vertices in this digraph
    private int E;                      // number of edges in this digraph
    private Bag<iDirectedEdge>[] adj;    // adj[v] = adjacency list for vertex v
    private int[] indegree;             // indegree[v] = indegree of vertex v

    private Node[] idToNode;
    private iModel model;
    private HashMap<Node, Integer> nodeToIntMap;
    private double totalTravelDistance;
    private double totalTravelTime;

    /**
     * Initializes an empty edge-weighted digraph with {@code V} vertices, 0 edges,
     * an ArrayList of ways, an Array returning Node from ID, a HashMap returning
     * ID from Node, an object of DistanceType, and a local version of model.
     *
     * @param V the number of vertices
     * @param idToNode an array to return a node from a given ID
     * @param nodeToIntMap a HashMap to return an ID from a given Node
     * @param model a copy of the Model class.
     * @throws IllegalArgumentException if {@code V < 0}
     */
    public EdgeWeightedDigraph(int V, Node[] idToNode, HashMap<Node, Integer> nodeToIntMap, iModel model) {
        this.nodeToIntMap = nodeToIntMap;
        this.idToNode = idToNode;
        this.model = model;

        if (V < 0) throw new IllegalArgumentException("Number of vertices in a Digraph must be nonnegative");
        this.V = V;
        this.E = 0;
        this.indegree = new int[V];

        adj = (Bag<iDirectedEdge>[]) new Bag[V];

        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<iDirectedEdge>();
        }
    }


    /**
     * Returns the number of vertices in this edge-weighted digraph.
     *
     * @return the number of vertices in this edge-weighted digraph
     */
    public int V() {
        return V;
    }

    /**
     * Returns the number of edges in this edge-weighted digraph.
     *
     * @return the number of edges in this edge-weighted digraph
     */
    public int E() {
        return E;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
    }

    /**
     * Adds the directed edge {@code e} to this edge-weighted digraph.
     *
     * @param e the edge
     * @throws IllegalArgumentException unless endpoints of edge are between {@code 0}
     *                                  and {@code V-1}
     */
    public void addEdge(iDirectedEdge e) {
        int v = e.from();
        int w = e.to();
        validateVertex(v);
        validateVertex(w);
        adj[v].add(e);
        indegree[w]++;
        E++;
    }

    /**
     * Returns the directed edges incident from vertex {@code v}.
     *
     * @param v the vertex
     * @return the directed edges incident from vertex {@code v} as an Iterable
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Iterable<iDirectedEdge> adj(int v) {
        validateVertex(v);
        return adj[v];
    }

    /**
     * Returns all directed edges in this edge-weighted digraph.
     * To iterate over the edges in this edge-weighted digraph, use foreach notation:
     * {@code for (DirectedEdge e : G.edges())}.
     *
     * @return all edges in this edge-weighted digraph, as an iterable
     */
    public Iterable<iDirectedEdge> edges() {
        Bag<iDirectedEdge> list = new Bag<iDirectedEdge>();
        for (int v = 0; v < V; v++) {
            for (iDirectedEdge e : adj(v)) {
                list.add(e);
            }
        }
        return list;
    }

    /**
     * Returns a string representation of this edge-weighted digraph.
     *
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
     * followed by the <em>V</em> adjacency lists of edges
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(V + " " + E + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v + ": ");
            for (iDirectedEdge e : adj[v]) {
                s.append(e + "  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }

    private double totalWeight;
    private double totalTime;

    /**
     * Creates a path consisting of edges between 2 given nodes and sets the model's
     * list of nodes to be drawn in the MapCanvas representing the route.
     *
     * @param node1 the from node
     * @param node2 the to node
     * @param transportType the transport type the person is using
     */
    public void pathFromTo(Node node1, Node node2, String transportType) {
        int v = nodeToIntMap.get(node1);
        int w = nodeToIntMap.get(node2);
        iDirectedEdge currentEdge = null;
        ArrayList<Node> nodeList = new ArrayList<>();
        Stack<iDirectedEdge> edgeList = null;
        try {
            edgeList = initDijkstra(v, w, transportType);
        } catch (NullPointerException e) {
            throw new NoPathException("No path exists from V: " + nodeToIntMap.get(node1) + " W: " + nodeToIntMap.get(node2) + " with the given transport type: " + transportType);
        }

        for (iDirectedEdge edge : edgeList) {
            double edgeWeight = edge.weight();
            double time = 0;
            if (transportType == "vehicle") {
                time = ((edgeWeight)/ edge.getMaxSpeed());

            }
            if (transportType == "bicycle") {
                time = (edge.weight()/ 15.5); // https://en.wikipedia.org/wiki/Preferred_walking_speed
            }
            if (transportType == "walking") {
                time = (edge.weight()/ 5); // https://en.wikipedia.org/wiki/Bicycle_performance
            }
            currentEdge = edge;
            nodeList.add(idToNode[edge.from()]);
            totalTime += time;
            totalWeight += edgeWeight;

        }
        nodeList.add(idToNode[currentEdge.to()]);
        model.setPathNodes(nodeList);
        model.setPathEdges(edgeList);

        totalTravelDistance = totalWeight;
        totalTravelTime = totalTime;
        totalWeight = 0;
        totalTime = 0;
    }

    /**
     * Calculcates if the EdgeWeightedDigraph has a path from ID v, to ID w
     * using only edges of the given transport type.
     *
     * @param v the from node's ID
     * @param w the to node's ID
     * @param transportType the transport type the person is using
     * @return Returns an Iterable<DirectedEdge> consisting of the edges between
     * the from and to node given in the fromTo() method.
     */
    private Stack<iDirectedEdge> initDijkstra(int v, int w, String transportType) {
        DijkstraSP sp = new DijkstraSP(this, v, transportType);
        if (sp.hasPathTo(w, transportType)) {
            return sp.pathTo(w, transportType);
        } else {
            return null;
        }
    }

    public Node[] getIdToNode() {
        return idToNode;
    }

    public Bag<iDirectedEdge>[] getAdj() {
        return adj;
    }

    public double getTotalTravelDistance() {
        return totalTravelDistance;
    }
    public double getTotalTravelTime(){
        return totalTravelTime;
    }
}

