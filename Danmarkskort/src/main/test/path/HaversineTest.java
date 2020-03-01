package path;

import Path.Haversine;
import TestClasses.StubModel;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HaversineTest {
    @Test
    public void testHaversine(){
        Haversine haversine = new Haversine();
        StubModel model = new StubModel();
        //Test case number and expected value taken from https://github.com/jasonwinn/haversine
        assertEquals(14.973190481586224,haversine.distance(47.6788206, -122.3271205, 47.6788206,-122.5271205, model),0.0001);
    }
}
