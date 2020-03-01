package Path;
import edu.princeton.cs.algs4.*;
/**
 *  The {@code DijkstraSP} class represents a data type for solving the
 *  single-source shortest paths problem in edge-weighted digraphs
 *  where the edge weights are nonnegative.
 *  <p>
 *  This implementation uses Dijkstra's algorithm with a binary heap.
 *
 *
 */
public class DijkstraSP {
    private double[] distTo;          // distTo[v] = distance  of shortest s->v path
    private iDirectedEdge[] edgeTo;    // edgeTo[v] = last edge on shortest s->v path
    private IndexMinPQ<Double> pq;    // priority queue of vertices

    private String transportType;


    /**
     * Computes a shortest-paths tree from the source vertex {@code s} to every other
     * vertex in the edge-weighted digraph {@code G}.
     *
     * @param  G the edge-weighted digraph
     * @param  s the source vertex
     * @param transportType the given transportation type
     * @throws IllegalArgumentException if an edge weight is negative
     * @throws IllegalArgumentException unless {@code 0 <= s < V}
     */
    public DijkstraSP(iDigraph G, int s, String transportType) {
        this.transportType = transportType;

        for (iDirectedEdge e : G.edges()) {
            if (e.weight() < 0)
                throw new IllegalArgumentException("edge " + e + " has negative weight");
        }

        distTo = new double[G.V()];
        edgeTo = new iDirectedEdge[G.V()];

        validateVertex(s);

        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;

        pq = new IndexMinPQ<Double>(G.V());
        pq.insert(s, distTo[s]);
        while (!pq.isEmpty())
        {
            int v = pq.delMin();
            if(transportType == "vehicle")
            {
                for (iDirectedEdge e : G.adj(v))
                {
                    if(e.getVehicleAllowed())
                    {
                        relax(e);
                    }
                }
            }
            if(transportType == "bicycle")
            {
                for (iDirectedEdge e : G.adj(v))
                {
                    if(e.getBicycleAllowed())
                    {
                        relax(e);
                    }
                }
            }
            if(transportType == "walking")
            {
                for (iDirectedEdge e : G.adj(v))
                {
                    if(e.getWalkingAllowed())
                    {
                        relax(e);
                    }
                }
            }
        }
        assert check(G, s);
    }

    /**
     * Relax edge e and update pq if changed
     *
     * @param e the directed edge
     */
    private void relax(iDirectedEdge e) {
        {
            int v = e.from(), w = e.to();
            if (distTo[w] > distTo[v] + e.weight()) {
                distTo[w] = distTo[v] + e.weight();
                edgeTo[w] = e;
                if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
                else pq.insert(w, distTo[w]);
            }
        }
    }


    /**
     * Returns true if there is a path from the source vertex {@code s} to vertex {@code v}.
     *
     * @param  v the destination vertex
     * @param transportType the transport type used
     * @return {@code true} if there is a path from the source vertex
     *         {@code s} to vertex {@code v}; {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public boolean hasPathTo(int v, String transportType) {
        validateVertex(v);
        if(transportType == "vehicle" && edgeTo[v].getVehicleAllowed())
        {
            return distTo[v] < Double.POSITIVE_INFINITY;
        }
        if(transportType == "bicycle" && edgeTo[v].getBicycleAllowed())
        {
            return distTo[v] < Double.POSITIVE_INFINITY;
        }
        if(transportType == "walking" && edgeTo[v].getWalkingAllowed())
        {
            return distTo[v] < Double.POSITIVE_INFINITY;
        }
        return false;
    }

    /**
     * Returns a shortest path from the source vertex {@code s} to vertex {@code v}.
     *
     * @param  v the destination vertex
     * @param transportType the transport type used
     * @return a shortest path from the source vertex {@code s} to vertex {@code v} of
     * a given transport type, as an iterable of edges, and {@code null} if no such path
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Stack<iDirectedEdge> pathTo(int v, String transportType) {
        validateVertex(v);
        if (!hasPathTo(v, transportType)) return null;
        Stack<iDirectedEdge> path = new Stack<iDirectedEdge>();
        for (iDirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()])
        {
            path.push(e);
        }
        return path;
    }

    /**
     * Checks that edge weights are nonnegative, distTo[v] and edgeTo[v] are
     * consistent, that all edges e = v->w satisfy distTo[w] <= distTo[v] + e.weight()
     * @param G the EdgeWeightedDigraph
     * @param s the source vertex
     * @return
     */
    private boolean check(iDigraph G, int s) {

        for (iDirectedEdge e : G.edges()) {
            if (e.weight() < 0) {
                return false;
            }
        }

        if (distTo[s] != 0.0 || edgeTo[s] != null) {
            return false;
        }
        for (int v = 0; v < G.V(); v++) {
            if (v == s) continue;
            if (edgeTo[v] == null && distTo[v] != Double.POSITIVE_INFINITY) {
                return false;
            }
        }

        for (int v = 0; v < G.V(); v++) {
            for (iDirectedEdge e : G.adj(v)) {
                int w = e.to();
                if (distTo[v] + e.weight() < distTo[w]) {
                    return false;
                }
            }
        }

        for (int w = 0; w < G.V(); w++) {
            if (edgeTo[w] == null) continue;
            iDirectedEdge e = edgeTo[w];
            int v = e.from();
            if (w != e.to()) return false;
            if (distTo[v] + e.weight() != distTo[w]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Validates the vertex
     *
     * @param v the given vertex
     */
    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        int V = distTo.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }
}



