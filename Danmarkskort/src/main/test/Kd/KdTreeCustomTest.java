package Kd;

import Draw.Drawable;
import TestClasses.StubDrawable;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class KdTreeCustomTest {


    //Boundary tests

    @Test
    public void testBoundaryNothingStickingOut(){
        TreeCustom kdTree=new TreeCustom();

        ArrayList<Drawable> list=new ArrayList<>();

        list.add(new StubDrawable(3,2,7,8,1));

        list.add(new StubDrawable(4,5, 6,5,2));

        list.add(new StubDrawable(3,4, 6,8,3));

        list.add(new StubDrawable(10,10,10,10,9));

        list.add(new StubDrawable(0,0,0,10,10));

        list.add(new StubDrawable(1,11,12,12,11));

        kdTree.insert(list,true);

        kdTree.getList(4,3,5,4);



       assertEquals(6, kdTree.getDrawables().size());

    }


    //Distance Tests

    @Test
    public void distanceTest(){
        TreeCustom kdTree=new TreeCustom();

        ArrayList<Drawable> list=new ArrayList<>();

        list.add(new StubDrawable(1,1,1,1,1));

        list.add(new StubDrawable(1,3,1,1,2));

        kdTree.insert(list,true);

        kdTree.getList(0,0,10,10);


    }

    //Nearest Neighbor Tests

    @Test
    public void nearestNeighborTestPoints(){
        TreeCustom kdTree=new TreeCustom();

        ArrayList<Drawable> list=new ArrayList<>();

        list.add(new StubDrawable(0,0,0,0,1));

        list.add(new StubDrawable(0,2,0,2,2));

        list.add(new StubDrawable(0,50,0,50,3));

        list.add(new StubDrawable(0,10,0,10,4));

        kdTree.insert(list,true);

        assertEquals(2, kdTree.nearest(0,2).getRepresentative().getFirstId());

        assertEquals(1, kdTree.nearest(0,-2).getRepresentative().getFirstId());

        assertEquals(1, kdTree.nearest(0,0).getRepresentative().getFirstId());

        assertEquals(2, kdTree.nearest(0,1).getRepresentative().getFirstId());

        assertEquals(4,kdTree.nearest(0,9).getRepresentative().getFirstId());

        assertEquals(4, kdTree.nearest(0,11).getRepresentative().getFirstId());

        assertEquals(3, kdTree.nearest(0,100).getRepresentative().getFirstId());

        assertEquals(4, kdTree.nearest(0,30).getRepresentative().getFirstId());

        assertEquals(3, kdTree.nearest(0,31).getRepresentative().getFirstId());


    }

    @Test
    public void nearestNeighborTestLinesTwoPoints(){
        TreeCustom kdTree=new TreeCustom();

        ArrayList<Drawable> list=new ArrayList<>();

        list.add(new StubDrawable(1,1,2,5,1));
        list.add(new StubDrawable(3,7,5,7,2));
        list.add(new StubDrawable(8,1,10,1,3));
        list.add(new StubDrawable(5,4,9,6,4));
        list.add(new StubDrawable(2,2,5,3,5));



        kdTree.insert(list,true);

        assertEquals(1, kdTree.nearest(1,1).getRepresentative().getFirstId());

        assertEquals(4, kdTree.nearest(10,10).getRepresentative().getFirstId());

        assertEquals(1, kdTree.nearest(0,0).getRepresentative().getFirstId());

        assertEquals(2, kdTree.nearest(2,7).getRepresentative().getFirstId());

        assertEquals(1, kdTree.nearest(2,5).getRepresentative().getFirstId());

        assertEquals(3, kdTree.nearest(10,1).getRepresentative().getFirstId());

        assertEquals(5, kdTree.nearest(5,0).getRepresentative().getFirstId());



    }

    @Test
    public void nearestNeighborTestLinesMultiplePoints(){
        TreeCustom kdTree=new TreeCustom();

        ArrayList<Drawable> list=new ArrayList<>();

        list.add(new StubDrawable(1,1,2,5, 2,4 , 1));
        list.add(new StubDrawable(3,7,5,7,6,7,2));
        list.add(new StubDrawable(8,1,10,1,8,3,3));
        list.add(new StubDrawable(5,4,9,6,10,5,4));
        list.add(new StubDrawable(2,2,5,3,7,7,5));



        kdTree.insert(list,true);

        assertEquals(1, kdTree.nearest(2,4).getRepresentative().getFirstId());

        assertEquals(4, kdTree.nearest(9,7).getRepresentative().getFirstId());

        assertEquals(1, kdTree.nearest(0,0).getRepresentative().getFirstId());

        assertEquals(2, kdTree.nearest(4,6).getRepresentative().getFirstId());

        assertEquals(3, kdTree.nearest(8,3).getRepresentative().getFirstId());

        assertEquals(4, kdTree.nearest(10,3).getRepresentative().getFirstId());

        assertEquals(5, kdTree.nearest(7,7).getRepresentative().getFirstId());



    }

    @Test
    public void nearestNeighborTestAdvanced(){
        TreeCustom kdTree=new TreeCustom();

        ArrayList<Drawable> list=new ArrayList<>();

        list.add(new StubDrawable(5,5,6,6, 1));
        list.add(new StubDrawable(2,5,3,6,2));
        list.add(new StubDrawable(1,8,2,9,3));
        list.add(new StubDrawable(1,1,1,2,4));
        list.add(new StubDrawable(6,2,7,3,5));
        list.add(new StubDrawable(8,4,9,5,6));
        list.add(new StubDrawable(7,7,8,8,7));



        kdTree.insert(list,true);

        assertEquals(5, kdTree.nearest(4,2).getRepresentative().getFirstId());
        assertEquals(2, kdTree.nearest(2,4).getRepresentative().getFirstId());
        assertEquals(4, kdTree.nearest(1,1).getRepresentative().getFirstId());

    }

}
