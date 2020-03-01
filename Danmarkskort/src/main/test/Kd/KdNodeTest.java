package Kd;

import Draw.Drawable;
import TestClasses.StubDrawable;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class KdNodeTest{


    @Test
    public void testDistance(){
        Node node1=new Node(null,null,null,false,0,0,0,0, false, new StubDrawable(0,0,0,0,1),null);
        Node node2=new Node(null,null,null,false,2,0,0,0, false,new StubDrawable(0,0,0,0,1),null);
        System.out.println(node1.distanceToMin(node2.getMinx(),node2.getMiny()));

      // assertEquals(node1.distance(node2.getMinx(),node2.getMiny()),2);

    }

}
