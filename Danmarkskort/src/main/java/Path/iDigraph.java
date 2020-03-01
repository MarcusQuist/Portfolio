package Path;

import OSM.Node;
import edu.princeton.cs.algs4.Bag;
/**
 * Digraph Interface
 */

public interface iDigraph {

    Iterable<iDirectedEdge> adj(int i);

    Iterable<iDirectedEdge> edges();
    String toString();

    Node[] getIdToNode();
    Bag<iDirectedEdge>[] getAdj();
    double getTotalTravelDistance();
    double getTotalTravelTime();
    int V();
}
