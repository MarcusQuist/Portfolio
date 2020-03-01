package All;

import Draw.WayType;
import TestClasses.StubModel;
import org.junit.Test;
import OSM.Parser;


public class ParserTest {

    Parser parser;
    StubModel model;

    @org.junit.Test
    public void testOneAddress(){
        try{
            model=new StubModel();
            parser=model.getParser();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

        //System.out.println(model.getWays().get(WayType.HIGHWAY_PRIMARY).get(0));

        //assertEquals(model.get,"TestVej 1337, 420tv, 6969 TestVille");

    }

    @Test
    public void testParserReadTag(){



    }


}
