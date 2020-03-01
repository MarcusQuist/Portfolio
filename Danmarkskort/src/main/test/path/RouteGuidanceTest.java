package path;

import Path.DirectedEdge;
import Path.DistanceType;
import Path.RouteGuidance;
import Path.iDirectedEdge;
import TestClasses.StubDirectedEdge;
import TestClasses.StubModel;
import edu.princeton.cs.algs4.Stack;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RouteGuidanceTest {

    @Test
    public void testCalculations() {
        StubModel model=new StubModel();
        RouteGuidance rg = new RouteGuidance(model,"vehicle");
        rg.calculations(1.0,2.0,3.0,4.0,5.0,5.0);
        assertEquals(-2000000000.0, rg.getCrossProduct(),0.00001);
    }

    @Test
    public void testCheckTurn(){

        StubModel model=new StubModel();

        StubDirectedEdge edge1 = new StubDirectedEdge("Rysagervej",2);
        StubDirectedEdge edge2 = new StubDirectedEdge("Rysagervej",2);
        StubDirectedEdge edge3 = new StubDirectedEdge("Blomstervej",3);

        Stack<iDirectedEdge> localEdge = new Stack<>();

        localEdge.push(edge1);

        localEdge.push(edge2);

        localEdge.push(edge3);

        model.setPathEdges(localEdge);

        RouteGuidance rg = new RouteGuidance(model,"vehicle");

        rg.checkTurn(8,11);

        assertEquals("Drive 10.0km down Rysagervej", rg.getRoute());

    }
}
