package All;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import static junit.framework.Assert.*;
public class AutocompleterTest
{
    HashMap<String, ArrayList<String>> streetMap;
    ArrayList<String> addressStringList = new ArrayList<>();
    ArrayList<String> addressStringList2 = new ArrayList<>();
    ArrayList<String> addressStringList3 = new ArrayList<>();

    Autocompleter ac;

    public AutocompleterTest(){
        //Initiliasis fiktiv addresses to use as test cases
        ac = new Autocompleter();
        ArrayList<Address> addressList = new ArrayList<>();
        streetMap = new HashMap<>();
        addressList.add(new Address("Lærkevej", "1", null, null, "3600", "Oelstykke", new OSM.Node(1, 22, 22)));
        addressList.add(new Address("Lærkevej", "2", null, null, "3600", "Oelstykke", new OSM.Node(2, 22, 22)));
        addressList.add(new Address("Lærkevej", "3", null, null, "3600", "Oelstykke", new OSM.Node(3, 22, 22)));
        addressList.add(new Address("Lærkevej", "4", null, null, "3600", "Oelstykke", new OSM.Node(4, 22, 22)));
        addressList.add(new Address("Lærkevej", "5", null, null, "3600", "Oelstykke", new OSM.Node(5, 22, 22)));
        for (Address a: addressList) addressStringList.add(a.toString());
        streetMap.put("Lærkevej, 3600 Oelstykke", addressStringList);
        addressList.clear();
        addressList.add(new Address("Andenvej", "1", null, null, "3700", "Andenby", new OSM.Node(6, 22, 22)));
        addressList.add(new Address("Andenvej", "2", null, null, "3700", "Andenby", new OSM.Node(7, 22, 22)));
        addressList.add(new Address("Andenvej", "3", null, null, "3700", "Andenby", new OSM.Node(8, 22, 22)));
        addressList.add(new Address("Andenvej", "4", null, null, "3700", "Andenby", new OSM.Node(9, 22, 22)));
        addressList.add(new Address("Andenvej", "5", null, null, "3700", "Andenby", new OSM.Node(10, 22, 22)));
        for (Address a: addressList) addressStringList2.add(a.toString());
        streetMap.put("Andenvej, 3700 Andenby", addressStringList2);
        addressList.clear();
        addressList.add(new Address("Tredjevej", "1", null, null, "3800", "Tredjeby", new OSM.Node(11, 22, 22)));
        addressList.add(new Address("Tredjevej", "2", null, null, "3800", "Tredjeby", new OSM.Node(12, 22, 22)));
        addressList.add(new Address("Tredjevej", "3", null, null, "3800", "Tredjeby", new OSM.Node(13, 22, 22)));
        addressList.add(new Address("Tredjevej", "4", null, null, "3800", "Tredjeby", new OSM.Node(14, 22, 22)));
        addressList.add(new Address("Tredjevej", "5", null, null, "3800", "Tredjeby", new OSM.Node(15, 22, 22)));
        for (Address a: addressList) addressStringList3.add(a.toString());
        streetMap.put("Tredjevej, 3800 Tredjeby", addressStringList3);
        addressList.clear();
    }

    @Test
    public void testStreetMapCreation() {
        //Makes sure streetMap is initialised as it would be in the actual code
        assertEquals(streetMap.get("Lærkevej, 3600 Oelstykke").get(0), "Lærkevej 1, 3600 Oelstykke");
        assertEquals(streetMap.get("Lærkevej, 3600 Oelstykke").get(1), "Lærkevej 2, 3600 Oelstykke");
        assertEquals(streetMap.get("Andenvej, 3700 Andenby").get(2), "Andenvej 3, 3700 Andenby");
        assertEquals(streetMap.get("Tredjevej, 3800 Tredjeby").get(3), "Tredjevej 4, 3800 Tredjeby");
        assertEquals(streetMap.get("Tredjevej, 3800 Tredjeby").get(3), "Tredjevej 4, 3800 Tredjeby");
    }

    @Test
    public void testAutocompleterStreetMatch() {
        assertEquals(addressStringList, ac.streetMatch(streetMap, "Lærkevej, 3600 Oelstykke"));
        assertEquals(addressStringList2, ac.streetMatch(streetMap, "Andenvej, 3700 Andenby"));
        assertEquals(addressStringList3, ac.streetMatch(streetMap, "Tredjevej, 3800 Tredjeby"));
        assertNull(ac.streetMatch(streetMap, "Tredjevej2"));
        assertNull(ac.streetMatch(streetMap, "lærkevej"));
        assertNull(ac.streetMatch(streetMap, "lærkevej "));
        assertNull(ac.streetMatch(streetMap, " lærkevej"));

        //Tests partial roads
        assertNull(ac.streetMatch(streetMap, "vej"));
        assertNull( ac.streetMatch(streetMap, "ndenvej"));
        assertNull( ac.streetMatch(streetMap, "redje"));
        assertNull(ac.streetMatch(streetMap, "Tredjevej"));
        assertNull(ac.streetMatch(streetMap, "Andenvej"));
        assertNull(ac.streetMatch(streetMap, "Lærkevej"));

        ;
    }

    @Test
    public void AutoCompleterMatchFullName(){
        assertEquals("Lærkevej 5, 3600 Oelstykke", ac.match(streetMap, "Lærkevej 5, 3600 Oelstykke").get(0));
        assertEquals("Lærkevej 4, 3600 Oelstykke", ac.match(streetMap, "Lærkevej 4, 3600 Oelstykke").get(0));
        assertEquals("Andenvej 2, 3700 Andenby", ac.match(streetMap, "Andenvej 2, 3700 Andenby").get(0));
    }

    @Test
    public void AutoCompleterMatchPartialName(){
        assertEquals("Lærkevej 5, 3600 Oelstykke", ac.match(streetMap, "Lærkevej 5").get(0));
        assertEquals("Lærkevej 4, 3600 Oelstykke", ac.match(streetMap, "Lærkevej 4").get(0));
        assertEquals("Andenvej 2, 3700 Andenby", ac.match(streetMap, "Andenvej 2").get(0));
    }

    @Test
    public void AutoCompleterMatchFailPartial(){
        assertTrue(ac.match(streetMap, "kevej 5, 3600 Oelstykke").isEmpty());
        assertTrue(ac.match(streetMap, "4, 3600 Oelstykke").isEmpty());
        assertTrue(ac.match(streetMap, "3700 Andenby").isEmpty());
    }

    @Test
    public void AutoCompleterMatchFailWrongOrder(){
        assertTrue(ac.match(streetMap, "3600 Oelstykke, Lærkevej 5").isEmpty());
        assertTrue(ac.match(streetMap, "3600 Lærkevej, 4 Oelstykke").isEmpty());
        assertTrue(ac.match(streetMap, "Andenby 3700, Andenvej 2").isEmpty());
    }

    @Test
    public void AutoCompleterMatchFailWrongInput(){
        assertEquals(new ArrayList<>(), ac.match(streetMap, "B"));
        assertEquals(new ArrayList<>(), ac.match(streetMap, "byer"));
        assertEquals(new ArrayList<>(), ac.match(streetMap, "jevekræl"));
        assertEquals(new ArrayList<>(), ac.match(streetMap, "ndenby"));
    }

    @Test
    public void AutoCompleterMatchAnyCase(){
        assertEquals("Lærkevej 5, 3600 Oelstykke", ac.match(streetMap, "lærkevej 5").get(0));
        assertEquals("Lærkevej 4, 3600 Oelstykke", ac.match(streetMap, "LÆRKEVEJ 4").get(0));
        assertEquals("Lærkevej 4, 3600 Oelstykke", ac.match(streetMap, "lÆRkEveJ 4").get(0));
    }

    @Test
    public void AutocompleterMatchTrailingSpaces(){
        assertEquals(5, ac.match(streetMap, "Tredjevej ").size());
        assertEquals(5, ac.match(streetMap, " Tredjevej ").size());
        assertEquals(5, ac.match(streetMap, "       Tredjevej     ").size());

        //Trailing spaces with housenumber
        assertNotSame(1, ac.match(streetMap, " Tredjevej 1").size());
        assertNotSame(1, ac.match(streetMap, "Tredjevej 1 ").size());
        assertNotSame(1, ac.match(streetMap, " Tredjevej 1 ").size());
    }

    @Test
    public void AutocompleterMatchRememberLastRoad()
    {
        assertEquals(5, ac.match(streetMap, "Tredjevej").size());
        assertEquals(5, ac.match(streetMap, "Tredjevej ").size());
        assertEquals(1, ac.match(streetMap, "Tredjevej 1").size());
    }


    @Test
    public void AutocompleterMatchRoadsWithSpaces(){
        ArrayList<Address> addressList = new ArrayList<>();
        ArrayList<String> addressStringList4 = new ArrayList<>();
        addressList.add(new Address("Tredjevej vej", "1", null, null, "3900", "Tredjeby2", new OSM.Node(11, 22, 22)));
        addressList.add(new Address("Tredjevej vej", "2", null, null, "3900", "Tredjeby2", new OSM.Node(12, 22, 22)));
        addressList.add(new Address("Tredjevej vej", "3", null, null, "3900", "Tredjeby2", new OSM.Node(13, 22, 22)));
        addressList.add(new Address("Tredjevej vej", "4", null, null, "3900", "Tredjeby2", new OSM.Node(14, 22, 22)));
        addressList.add(new Address("Tredjevej vej", "5", null, null, "3900", "Tredjeby2", new OSM.Node(15, 22, 22)));
        for (Address a: addressList) addressStringList4.add(a.toString());
        streetMap.put("Tredjevej vej, 3900 Tredjeby2", addressStringList4);
        assertEquals(2, ac.match(streetMap, "Tredjevej").size());
        assertEquals(2, ac.match(streetMap, "Tredjevej ").size());
        assertEquals(1, ac.match(streetMap, "Tredjevej 1").size());
        assertEquals(2, ac.match(streetMap, "Tredjevej ").size());
        assertEquals(2, ac.match(streetMap, "Tredjevej ").size());
        assertEquals(5, ac.match(streetMap, "Tredjevej v").size());
    }
}
